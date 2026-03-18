# Member 3 - Furniture Library Module Documentation

## Overview

The Furniture Library module is responsible for:
1. **Displaying furniture items** in a comprehensive library interface
2. **Managing furniture data** (properties, dimensions, materials)
3. **Integrating with the 2D room editor** for furniture placement
4. **Providing 3D preview functionality** (future integration)

## Current Implementation Status

### ✅ Completed Features

#### 1. Core Data Models
- **FurnitureItem.java**: Represents a single furniture item with properties:
  - Name, Type, Image Path
  - Dimensions (Width, Height, Depth)
  - Material, Description
  
- **FurnitureType.java**: Enum for furniture categories
  - CHAIR, TABLE, SOFA, BED, DESK, CABINET, LAMP, OTHER

- **FurnitureDefaults.java**: Default furniture dataset with 9 sample items
  - Wooden Chair, Office Chair
  - Dining Table, Coffee Table
  - 3-Seater Sofa
  - Queen Bed
  - Office Desk
  - Bookshelf
  - Table Lamp

#### 2. UI Components

**FurnitureLibraryPanel.java**
- Left sidebar: Furniture list with thumbnail images
- Category filter dropdown (All, Chairs, Tables, Sofas, Beds, Desks, Cabinets, Lamps)
- Right panel: Properties display
- Split pane layout with resizable divider
- Professional styling with consistent color scheme (#F5F5F5 background)

**PropertiesPanel.java**
- Large furniture preview image (200x200 px)
- Item details grid:
  - Name (bold), Type, Dimensions (W × H × D), Material
  - Description with text wrapping
- Action buttons:
  - "Add to Room" (green) - Adds furniture to current room design
  - "View 3D" (blue) - Views furniture in 3D renderer
- Placeholder image generation for missing resources

#### 3. Controllers & Services

**FurnitureController.java**
- Loads furniture items from FurnitureDefaults
- Provides furniture access by name or full list
- Simple in-memory storage

**FurnitureListCellRenderer**
- Custom list cell rendering with thumbnails (50x50 px)
- Graceful fallback to placeholder icons for missing images
- Displays item name below thumbnail

#### 4. Image Resources
- Placeholder images generated in `/src/main/resources/images/`
- Both preview (200x200 px) and thumbnail (50x50 px) sizes
- Color-coded by furniture type for visual distinction

### 🔄 Integration Points

#### With DesignState (Member 1)
```java
// Future: Track selected furniture in shared state
designState.setSelectedFurniture(furnitureItem);
```

#### With 2D Editor (Member 4)
```
Workflow:
1. User selects furniture in FurnitureLibraryPanel
2. User clicks "Add to Room" button
3. Navigation to 2D Editor with selected furniture pre-loaded
4. User places furniture in room using 2D tools
```

#### With 3D Renderer (Member 5)
```
Workflow:
1. User selects furniture in FurnitureLibraryPanel
2. User clicks "View 3D" button
3. Navigation to 3D Viewer with furniture item displayed
4. User can inspect 3D model and properties
```

#### With Room Module (Member 2)
```
- Furniture items placed in rooms via 2D Editor
- Room template system stores furniture positions
- RoomRepository manages persistence
```

## File Structure

```
com.teamname.furniviz.furniture/
├── FurnitureItem.java              (Data Model)
├── FurnitureType.java              (Enum)
├── FurnitureDefaults.java          (Sample Data)
├── FurnitureController.java        (Business Logic)
├── FurnitureLibraryPanel.java      (Main UI)
├── PropertiesPanel.java            (Details UI)
├── FurnitureLibraryService.java    (Integration Service)
└── ...

resources/
└── images/
    ├── wooden_chair.png / wooden_chair_thumb.png
    ├── office_chair.png / office_chair_thumb.png
    ├── dining_table.png / dining_table_thumb.png
    ├── coffee_table.png / coffee_table_thumb.png
    ├── sofa.png / sofa_thumb.png
    ├── bed.png / bed_thumb.png
    ├── desk.png / desk_thumb.png
    ├── bookshelf.png / bookshelf_thumb.png
    └── lamp.png / lamp_thumb.png
```

## Usage

### Running from Navigator
```java
// In Navigator.java - Already integrated!
JButton furnitureButton = new JButton("Furniture Library");
furnitureButton.addActionListener(e -> showFurniture());

public void showFurniture() {
    layout.show(this, "FURNITURE");
}
```

### Accessing from Main App
1. Click "Furniture Library" button on Home dashboard
2. Browse furniture items in the left panel
3. Select an item to view details in the right panel
4. Use action buttons to add to room or view in 3D

## Future Enhancements (TODO List)

### Phase 2: Advanced Features
- [ ] Category filtering (currently shows all items)
- [ ] Search/filter by name or properties
- [ ] Furniture customization (color, material selection)
- [ ] Favorite/recent items tracking
- [ ] User-uploaded custom furniture items

### Phase 3: Database Integration
- [ ] Load furniture from MongoDB (instead of hardcoded defaults)
- [ ] Furniture database management (CRUD operations)
- [ ] Furniture inventory system
- [ ] Multiple furniture libraries/collections

### Phase 4: 3D & Advanced Rendering
- [ ] 3D model integration with renderer3d module
- [ ] Material/texture preview
- [ ] Cost/pricing display for each item
- [ ] Furniture compatibility checking

## Integration Contract with 2D Editor

When a furniture item is added to a room:

```java
// 1. FurnitureLibraryService notifies DesignState
designState.setSelectedFurniture(furnitureItem);

// 2. Navigator switches to 2D Editor
navigator.show2D();

// 3. Canvas2D reads selected furniture
FurnitureItem selectedItem = designState.getSelectedFurniture();

// 4. User places furniture in room
// Canvas2D adds PlacedFurniture object to current room
room.addFurniture(new PlacedFurniture(
    selectedItem,
    x,              // Position X
    y,              // Position Y
    rotation,       // Rotation angle
    scale           // Scale factor
));
```

## Integration Contract with 3D Renderer

When viewing furniture in 3D:

```java
// 1. FurnitureLibraryService notifies DesignState
designState.setSelectedFurniture(furnitureItem);

// 2. Navigator switches to 3D Viewer
navigator.show3D();

// 3. Renderer3D reads selected furniture
FurnitureItem selectedItem = designState.getSelectedFurniture();

// 4. MeshFactory creates 3D model
Mesh mesh = meshFactory.createFurnitureMesh(selectedItem);

// 5. Renderer displays mesh
renderer.render(mesh);
```

## Adding New Furniture Items

To add new furniture items:

1. Create PNG image files (both 50x50 and 200x200 sizes)
2. Place in `/src/main/resources/images/`
3. Add to `FurnitureDefaults.getDefaultFurniture()`:

```java
items.add(new FurnitureItem(
    "Item Name",
    FurnitureType.CHAIR,
    "/images/item_name.png",
    width,      // in meters
    height,     // in meters
    depth,      // in meters
    "Material",
    "Description"
));
```

## Testing

### Standalone Testing
Run the Furniture Library independently:
```bash
java -cp "target/classes;lib/*" com.teamname.furniviz.furniture.FurnitureLibraryPanel
```

### Integration Testing
1. Run the full application: `java -cp "target/classes;lib/*" com.teamname.furniviz.app.App`
2. From Home dashboard, click "Furniture Library"
3. Verify:
   - [ ] All 9 furniture items display
   - [ ] Thumbnail images load correctly
   - [ ] Item details display on selection
   - [ ] Placeholder image shows for missing images
   - [ ] "Add to Room" button is clickable
   - [ ] "View 3D" button is clickable

## Known Issues & Limitations

1. **Image Loading**: Images must be in classpath resources folder
2. **Filtering**: Category filter dropdown not yet implemented
3. **Persistence**: Furniture data is hardcoded (no database connection yet)
4. **Customization**: Cannot customize furniture properties in UI yet
5. **3D Integration**: View 3D button shows placeholder message only

## Technical Notes

### Resource Loading
- Images are loaded via `getClass().getResource(path)`
- Path format: `/images/filename.png`
- Both preview and thumbnail images generated dynamically

### Placeholder Image Generation
- If an image resource is missing, a placeholder is generated
- Placeholder uses appropriate furniture type color
- Label text is displayed on larger placeholders (200x200)

### UI Styling
- Background color: #F5F5F5 (light gray)
- Button colors:
  - Back button: #C8C8C8 (light gray)
  - Add to Room: #4CAF50 (green)
  - View 3D: #2196F3 (blue)
- Font: Segoe UI (Windows default UI font)

---

**Member 3 Responsibility**: Member 3 (Furniture Handling) is responsible for maintaining and extending this module. Changes should follow the established patterns and notify the integrator (Member 1) when new integration points are required.

**Last Updated**: March 15, 2026

