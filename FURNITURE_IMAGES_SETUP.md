# ✅ FURNITURE IMAGE SETUP - COMPLETE GUIDE

## TLDR (The Answer)

### **Where to put furniture images?**
```
C:\Users\lap.lk\Desktop\Furniture-Visualizer\src\main\resources\images\
```

**That's it. Copy all PNG images there.**

---

## Current Status

✅ **Image folder exists and is ready**
✅ **18 placeholder files already in place** (9 images + 9 thumbnails)
✅ **All filenames are correct**
✅ **Structure is perfect**

### What's Currently in the Folder

```
18 files total:
✓ bed.png + bed_thumb.png
✓ bookshelf.png + bookshelf_thumb.png
✓ coffee_table.png + coffee_table_thumb.png
✓ desk.png + desk_thumb.png
✓ dining_table.png + dining_table_thumb.png
✓ lamp.png + lamp_thumb.png
✓ office_chair.png + office_chair_thumb.png
✓ sofa.png + sofa_thumb.png
✓ wooden_chair.png + wooden_chair_thumb.png
```

---

## What You Need to Do

### **STEP 1: Download 9 PNG Images**

Go to **unsplash.com** and download images for:

```
1. Wooden Chair      → wooden_chair.png
2. Office Chair      → office_chair.png
3. Dining Table      → dining_table.png
4. Coffee Table      → coffee_table.png
5. 3-Seater Sofa     → sofa.png
6. Queen Bed         → bed.png
7. Office Desk       → desk.png
8. Bookshelf         → bookshelf.png
9. Table Lamp        → lamp.png
```

**Search Tips:**
- Search on Unsplash: "[furniture name] product photography white background"
- Choose clear, professional-looking images
- Download as PNG (not JPEG)

### **STEP 2: Rename Files**

When you download from Unsplash, file will be named something like: `unsplash_abc123.png`

**Rename to:** `wooden_chair.png`, `office_chair.png`, etc.

**How to rename:**
1. Right-click the file
2. Click "Rename"
3. Type the new name from the table above
4. Press Enter

### **STEP 3: Copy to Project**

Move all 9 files to:
```
C:\Users\lap.lk\Desktop\Furniture-Visualizer\src\main\resources\images\
```

**How to copy:**
1. Open Windows Explorer
2. Go to Downloads (where you downloaded the files)
3. Select all 9 files (Ctrl+A or select individually)
4. Right-click → "Copy" (or Ctrl+C)
5. Navigate to: `C:\Users\lap.lk\Desktop\Furniture-Visualizer\src\main\resources\images\`
6. Right-click in empty space → "Paste" (or Ctrl+V)

### **STEP 4: Verify Files**

After copying, open Windows Explorer and navigate to:
```
C:\Users\lap.lk\Desktop\Furniture-Visualizer\src\main\resources\images\
```

You should see **18 files** total (9 new + 9 existing):
```
✓ All 9 furniture images you downloaded
✓ All 9 thumbnail images (keep existing ones)
```

### **STEP 5: Rebuild Project**

In IntelliJ IDEA:
1. Click **Build** menu
2. Click **Rebuild Project**
3. Or press **Ctrl+F9**

### **STEP 6: Test**

1. Click **Run** → **Run App**
2. Click "**Furniture Library**" button
3. Verify:
   - List shows 9 items with thumbnails
   - Click each item
   - Preview image appears on right panel
   - All images display correctly ✅

---

## File Organization

### How Files Are Used

```
Your Downloaded Image: wooden_chair.png (600x600px)
                           ↓
                      Application loads
                           ↓
                    ┌──────┴──────┐
                    ↓             ↓
            List Thumbnail     Preview Panel
            (50x50px)          (220x220px)
            Auto-scaled        Auto-scaled
            from full          from full
```

### File Structure After Setup

```
📁 Furniture-Visualizer\
└── 📁 src\main\resources\
    └── 📁 images\
        ├── bed.png                    ✅ You download this
        ├── bed_thumb.png              (Existing - keep)
        ├── bookshelf.png              ✅ You download this
        ├── bookshelf_thumb.png        (Existing - keep)
        ├── coffee_table.png           ✅ You download this
        ├── coffee_table_thumb.png     (Existing - keep)
        ├── desk.png                   ✅ You download this
        ├── desk_thumb.png             (Existing - keep)
        ├── dining_table.png           ✅ You download this
        ├── dining_table_thumb.png     (Existing - keep)
        ├── lamp.png                   ✅ You download this
        ├── lamp_thumb.png             (Existing - keep)
        ├── office_chair.png           ✅ You download this
        ├── office_chair_thumb.png     (Existing - keep)
        ├── sofa.png                   ✅ You download this
        ├── sofa_thumb.png             (Existing - keep)
        ├── wooden_chair.png           ✅ You download this
        └── wooden_chair_thumb.png     (Existing - keep)
```

---

## Image Requirements

### Format
- **Must be PNG** (not JPEG, GIF, BMP, or any other format)
- Background: White or transparent (recommended)

### Size
- **Minimum:** 400×400 pixels
- **Recommended:** 600×600 pixels or larger
- **File size:** Typically 200-500 KB per image

### Quality
- Clear and sharp
- Professional product photo style
- Well-lit
- Front-facing view
- Clean background

---

## Best Places to Download

### Free High-Quality Photo Sites ⭐

1. **Unsplash.com** (Recommended)
   - Free high-quality images
   - Great furniture selection
   - No login required
   - Commercial use allowed

2. **Pexels.com**
   - Free royalty-free images
   - Good furniture collection

3. **Pixabay.com**
   - Free commercial use
   - Large furniture catalog

### For Illustrated Furniture

4. **Freepik.com**
   - Free vectors (save as PNG)
   - Design-style furniture

5. **Flaticon.com**
   - Free furniture icons
   - Download as PNG

### AI-Generated (If Needed)

6. **DALL-E** (openai.com/dall-e)
7. **Midjourney** (midjourney.com)
8. **Stable Diffusion** (online generators)

**Example AI Prompt:**
```
"Product photo of a wooden chair, white background, 
professional photography, high quality, front view, 4k"
```

---

## Troubleshooting

### Images Don't Show in App?

1. **Check Folder Path**
   ```
   Verify exactly: C:\Users\lap.lk\Desktop\Furniture-Visualizer\src\main\resources\images\
   ```

2. **Check Filenames**
   ```
   CORRECT:     wooden_chair.png (lowercase, underscores)
   WRONG:       Wooden_Chair.png (capitals)
   WRONG:       wooden-chair.png (dash instead of underscore)
   WRONG:       wooden chair.png (space)
   ```

3. **Check Format**
   ```
   CORRECT:     wooden_chair.png (PNG format)
   WRONG:       wooden_chair.jpg (JPEG format)
   WRONG:       wooden_chair.gif (GIF format)
   ```

4. **Check File Exists**
   - Open Windows Explorer
   - Navigate to the folder
   - Verify the file is there

5. **Rebuild Project**
   ```
   IntelliJ: Ctrl+F9
   ```

6. **Restart Application**
   - Close the app
   - Run it again
   - Test furniture library

---

## Helpful Tools Created

I've created several guides in your project folder:

### **Quick Start Guides**
- ✅ **IMAGES_QUICK_START.md** - 5-minute quick start
- ✅ **FURNITURE_IMAGE_DOWNLOAD_GUIDE.md** - Complete download guide
- ✅ **IMAGE_DOWNLOAD_CHECKLIST.md** - Step-by-step with checklist
- ✅ **FILE_STRUCTURE_GUIDE.md** - Detailed file organization

### **Utility Scripts**
- ✅ **VERIFY_IMAGES.bat** - Double-click to verify your setup

---

## Extending Later: Adding More Furniture

When you want to add more furniture items later:

### 1. Add Image Files
- Download PNG for new furniture
- Name it: `armchair.png`
- Save to: `src/main/resources/images/`

### 2. Update Code
Edit `FurnitureDefaults.java`:

```java
// Add new furniture item
items.add(new FurnitureItem(
    "Armchair",                    // Name shown in app
    FurnitureType.CHAIR,          // Category
    "/images/armchair.png",       // Path to image (must match filename)
    0.7, 0.85, 0.75,              // Width, Height, Depth in meters
    "Fabric",                      // Material
    "Comfortable armchair."        // Description
));
```

### 3. Test
- Rebuild project
- Run app
- Test furniture library

---

## Connection to Room System

Your Furniture Library Panel connects to the room system through:

```
FurnitureLibraryPanel
    ↓
"Add to Room" Button
    ↓
DesignState (shared data)
    ↓
2D Editor (Member 4)
    ↓
3D View (Member 5)
```

When a user clicks "Add to Room":
1. Selected furniture item is stored in DesignState
2. 2D Editor receives the item
3. Item appears in the room design
4. 3D view renders it in 3D

---

## What Happens at Runtime

### When App Loads
```
1. FurnitureDefaults.getDefaultFurniture()
2. Load 9 furniture items with image paths
3. FurnitureLibraryPanel displays list
4. For each item, load image from: /images/wooden_chair.png
5. Auto-scale to 50x50px for thumbnail
6. Store full image for preview
```

### When User Selects Item
```
1. User clicks "Wooden Chair" in list
2. PropertiesPanel loads full image
3. Auto-scale to 220x220px
4. Display name, dimensions, material, description
5. Ready for "Add to Room" or "View 3D"
```

### When User Clicks "Add to Room"
```
1. Selected furniture added to DesignState
2. 2D Editor receives notification
3. Item appears in room design
4. Ready for rotation, movement, scaling
5. Ready for 3D export
```

---

## Important Notes

✅ **All files are PNG only** - No JPEG, GIF, or other formats

✅ **Filenames must match exactly** - `wooden_chair.png` not `Wooden_Chair.png`

✅ **Images auto-scale** - Provide full-size, app handles scaling

✅ **Thumbnails auto-generate** - No manual thumbnail creation needed

✅ **Transparent backgrounds OK** - White background recommended

✅ **High resolution better** - 600x600px is ideal

---

## Quick Checklist

### Before Download
- [ ] Unsplash.com or similar site ready
- [ ] Know the 9 furniture items needed
- [ ] Have 30 minutes available

### During Download
- [ ] Search each item on Unsplash
- [ ] Choose high-quality images
- [ ] Download as PNG
- [ ] Rename to correct names
- [ ] Save to Downloads folder

### After Download
- [ ] All 9 files downloaded
- [ ] All filenames correct (lowercase, underscores)
- [ ] All files are PNG format
- [ ] Copy to `src/main/resources/images\`
- [ ] Verify 18 total files in folder

### Testing
- [ ] Rebuild project (Ctrl+F9)
- [ ] Run app (Shift+F10)
- [ ] Click "Furniture Library"
- [ ] Select each item
- [ ] Verify images display

---

## Summary Table

| What | Where | How | When |
|------|-------|-----|------|
| **Download images** | unsplash.com | Search and download PNG | Before copying |
| **Rename files** | Downloads folder | Right-click → Rename | After download |
| **Copy to project** | `src/main/resources/images/` | Copy-paste or drag-drop | After renaming |
| **Verify setup** | Windows Explorer | Count files (should be 18) | After copying |
| **Rebuild project** | IntelliJ | Build → Rebuild Project | After copying |
| **Test app** | Run application | Click Furniture Library | After rebuild |

---

## You're Ready! 🚀

**Your folder structure is perfect. Everything is in place.**

**All you need to do is:**
1. Download 9 PNG images from unsplash.com
2. Rename them correctly
3. Copy to the folder above
4. Rebuild and test

**Questions?** Check the detailed guides created in your project folder.

**Let's go!** 📥🛋️🪑🛏️✨

