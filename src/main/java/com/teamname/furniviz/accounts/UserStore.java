package com.teamname.furniviz.accounts;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.IndexOptions;
import com.teamname.furniviz.storage.MongoDBConnection;
import org.bson.Document;
import org.bson.conversions.Bson;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

public class UserStore {
    private static final String COLLECTION_NAME = "users";
    private MongoCollection<Document> usersCollection;

    public UserStore() {
        MongoDBConnection connection = MongoDBConnection.getInstance();
        this.usersCollection = connection.getCollection(COLLECTION_NAME);
        ensureCollectionsExist();
    }

    public void ensureCollectionsExist() {
        try {
            usersCollection.createIndex(
                    new Document("email", 1),
                    new IndexOptions().unique(true)
            );
            System.out.println("[OK] Users collection ready with email index");
        } catch (Exception e) {
            System.out.println("[INFO] Users collection/index already exists");
        }
    }

    public User registerUser(String email, String hashedPassword, String firstName, String lastName)
            throws Exception {
        if (emailExists(email)) {
            throw new Exception("Email already registered");
        }

        try {
            User user = new User(email, hashedPassword, firstName, lastName);

            Document userDoc = new Document()
                    .append("_id", user.getUserId())
                    .append("email", user.getEmail())
                    .append("password", user.getHashedPassword())
                    .append("firstName", user.getFirstName())
                    .append("lastName", user.getLastName())
                    .append("role", user.getRole().name())
                    .append("createdAt", convertToDate(user.getCreatedAt()))
                    .append("lastLogin", null)
                    .append("isActive", user.isActive());

            usersCollection.insertOne(userDoc);
            System.out.println("[OK] User registered: " + email);
            return user;
        } catch (com.mongodb.MongoWriteException e) {
            if (e.getError().getCategory().toString().contains("DUPLICATE")) {
                throw new Exception("Email already registered");
            }
            throw e;
        }
    }

    public User findByEmail(String email) {
        try {
            Bson filter = Filters.eq("email", email);
            Document doc = usersCollection.find(filter).first();

            if (doc == null) {
                return null;
            }

            return documentToUser(doc);
        } catch (Exception e) {
            System.err.println("[ERROR] Error finding user by email: " + e.getMessage());
            return null;
        }
    }

    public User findById(String userId) {
        try {
            Bson filter = Filters.eq("_id", userId);
            Document doc = usersCollection.find(filter).first();

            if (doc == null) {
                return null;
            }

            return documentToUser(doc);
        } catch (Exception e) {
            System.err.println("[ERROR] Error finding user by ID: " + e.getMessage());
            return null;
        }
    }

    public boolean emailExists(String email) {
        try {
            Bson filter = Filters.eq("email", email);
            Document doc = usersCollection.find(filter).first();
            return doc != null;
        } catch (Exception e) {
            System.err.println("[ERROR] Error checking email existence: " + e.getMessage());
            return false;
        }
    }

    public boolean updateUser(User user) {
        try {
            Bson filter = Filters.eq("_id", user.getUserId());
            Document updateDoc = new Document()
                    .append("email", user.getEmail())
                    .append("password", user.getHashedPassword())
                    .append("firstName", user.getFirstName())
                    .append("lastName", user.getLastName())
                    .append("role", user.getRole().name())
                    .append("lastLogin", convertToDate(user.getLastLogin()))
                    .append("isActive", user.isActive());

            usersCollection.updateOne(
                    filter,
                    new Document("$set", updateDoc)
            );
            System.out.println("[OK] User updated: " + user.getEmail());
            return true;
        } catch (Exception e) {
            System.err.println("[ERROR] Error updating user: " + e.getMessage());
            return false;
        }
    }

    public boolean deleteUser(String userId) {
        try {
            Bson filter = Filters.eq("_id", userId);
            Document updateDoc = new Document("isActive", false);
            usersCollection.updateOne(
                    filter,
                    new Document("$set", updateDoc)
            );
            System.out.println("[OK] User deactivated: " + userId);
            return true;
        } catch (Exception e) {
            System.err.println("[ERROR] Error deleting user: " + e.getMessage());
            return false;
        }
    }

    private User documentToUser(Document doc) {
        return new User(
                doc.getString("_id"),
                doc.getString("email"),
                doc.getString("password"),
                doc.getString("firstName"),
                doc.getString("lastName"),
                Role.fromString(doc.getString("role")),
                convertToLocalDateTime(doc.getDate("createdAt")),
                doc.getDate("lastLogin") != null ? convertToLocalDateTime(doc.getDate("lastLogin")) : null,
                doc.getBoolean("isActive")
        );
    }

    private Date convertToDate(LocalDateTime ldt) {
        if (ldt == null) return null;
        return Date.from(ldt.atZone(ZoneId.systemDefault()).toInstant());
    }

    private LocalDateTime convertToLocalDateTime(Date date) {
        if (date == null) return null;
        return date.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
    }
}
