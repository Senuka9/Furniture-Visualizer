# Ownership & Responsibility Matrix

## Team Member Assignment

| Module | Packages | Member | Responsibility |
|--------|----------|--------|-----------------|
| **Application & UI** | `app/`, `auth/` | Member 1 | Main application frame, navigation, login interface |
| **Accounts** | `accounts/` | Member 1 | User management, roles, account controller (FAQ requirement) |
| **Room Management** | `room/` | Member 2 | Room creation, shape definitions, room form UI |
| **Furniture Tools** | `furniture/` | Member 3 | Furniture library, properties panel, furniture management |
| **2D Editor** | `editor2d/` | Member 4 | 2D canvas, selection, drag & drop, drawing |
| **3D Renderer** | `renderer3d/` | Member 5 | 3D rendering engine, camera, projection, mesh factory |
| **Storage & Portfolio** | `storage/`, `portfolio/` | Member 6 | Design save/load, JSON utilities, portfolio view |
| **Shared Utilities** | `common/` | **Integrator** | UI theme, dialogs, validation, file utilities |

## Demo Classes

Each member should implement independent demo/test class:
- `DemoLogin.java` - Login and authentication testing
- `DemoRoom.java` - Room management testing
- `Demo2D.java` - 2D editor testing
- `Demo3D.java` - 3D renderer testing
- `DemoStorage.java` - Storage and save/load testing

## Package Structure

```
com.teamname.furniviz
├── app/              → Member 1 (Application core)
├── auth/             → Member 1 (Authentication - LoginPanel only)
├── accounts/         → Member 1 (User management - NEW)
├── room/             → Member 2 (Room management)
├── furniture/        → Member 3 (Furniture handling)
├── editor2d/         → Member 4 (2D editing)
├── renderer3d/       → Member 5 (3D rendering)
├── storage/          → Member 6 (Data persistence)
├── portfolio/        → Member 6 (Design gallery)
├── common/           → Integrator (Shared - use carefully)
└── demo/             → All members (Testing)
```

## Integration Notes

- **Common Package**: Only modify `common/ui/` and `common/util/` when adding genuinely shared resources
- **Dependencies**: Try to minimize cross-module dependencies; communicate through `DesignState` when needed
- **Testing**: Use demo classes to test your modules independently before integration
