# 📥 FURNITURE IMAGE DOWNLOAD GUIDE

## Where to Put Images

**Folder Path:**
```
C:\Users\lap.lk\Desktop\Furniture-Visualizer\src\main\resources\images\
```

This is where ALL furniture images should go.

---

## What Images You Need

Your furniture list has **9 items** that need images:

### Required Images (Full-Size Preview Images)

| Furniture Item | Required Filename | Category | Size Recommendation |
|---|---|---|---|
| Wooden Chair | `wooden_chair.png` | Chairs | 400x400px minimum |
| Office Chair | `office_chair.png` | Chairs | 400x400px minimum |
| Dining Table | `dining_table.png` | Tables | 400x400px minimum |
| Coffee Table | `coffee_table.png` | Tables | 400x400px minimum |
| 3-Seater Sofa | `sofa.png` | Sofas | 400x400px minimum |
| Queen Bed | `bed.png` | Beds | 400x400px minimum |
| Office Desk | `desk.png` | Desks | 400x400px minimum |
| Bookshelf | `bookshelf.png` | Cabinets | 400x400px minimum |
| Table Lamp | `lamp.png` | Lamps | 400x400px minimum |

**TOTAL: 9 PNG images needed**

---

## Best Sources to Download Furniture Images

### Option 1: Free Stock Photo Sites (Recommended)
- **Unsplash** (unsplash.com) - Free high-quality images
- **Pexels** (pexels.com) - Free commercial use
- **Pixabay** (pixabay.com) - Free royalty-free images
- **Freepik** (freepik.com) - Free furniture vectors (download as PNG)

### Option 2: 3D Model Sites (Export as PNG)
- **Sketchfab** (sketchfab.com) - Download 3D models, render as PNG
- **Turbosquid** (turbosquid.com) - Premium models
- **CGTrader** (cgtrader.com) - 3D model marketplace

### Option 3: Generate with AI (Free)
- **Midjourney** (midjourney.com) - AI image generation
- **DALL-E** (openai.com/dall-e) - AI image generator
- **Stable Diffusion** - Free AI image generation

**Example prompts:**
- "Modern wooden chair, white background, high quality product photo"
- "Office ergonomic chair, isometric view, product photography"
- "Dining table wooden, top view, professional product photo"

---

## Step-by-Step Download Instructions

### Step 1: Download Each Image
1. Go to one of the recommended sites above
2. Search for each furniture item (e.g., "wooden chair PNG")
3. Download as PNG format
4. **Keep image dimensions at least 400x400px**

### Step 2: Name Files Correctly
After downloading, rename each file to match the table above exactly:
- ✓ `wooden_chair.png`
- ✓ `office_chair.png`
- ✓ `dining_table.png`
- ✓ `coffee_table.png`
- ✓ `sofa.png`
- ✓ `bed.png`
- ✓ `desk.png`
- ✓ `bookshelf.png`
- ✓ `lamp.png`

### Step 3: Place Files in Correct Folder
Copy all 9 PNG files to:
```
C:\Users\lap.lk\Desktop\Furniture-Visualizer\src\main\resources\images\
```

### Step 4: Rebuild Project
In IntelliJ IDEA:
- Click Build → Rebuild Project
- Or press Ctrl+F9

### Step 5: Run and Test
- Run the application
- Click "Furniture Library"
- Select each furniture item
- Verify the preview image shows on the right panel

---

## Quick Download Checklist

- [ ] **Wooden Chair** - Downloaded as `wooden_chair.png`
- [ ] **Office Chair** - Downloaded as `office_chair.png`
- [ ] **Dining Table** - Downloaded as `dining_table.png`
- [ ] **Coffee Table** - Downloaded as `coffee_table.png`
- [ ] **3-Seater Sofa** - Downloaded as `sofa.png`
- [ ] **Queen Bed** - Downloaded as `bed.png`
- [ ] **Office Desk** - Downloaded as `desk.png`
- [ ] **Bookshelf** - Downloaded as `bookshelf.png`
- [ ] **Table Lamp** - Downloaded as `lamp.png`

All files placed in:
- [ ] `C:\Users\lap.lk\Desktop\Furniture-Visualizer\src\main\resources\images\`

Project rebuilt:
- [ ] Click Build → Rebuild Project

App tested:
- [ ] Run app
- [ ] Furniture Library works
- [ ] Images display correctly

---

## Image Format & Quality Requirements

### PNG Format
✓ Transparent background recommended
✓ Lossless compression
✓ Best for product images
✓ Works perfectly with Java Swing

### Recommended Specifications
- **Format**: PNG (24-bit or 32-bit with transparency)
- **Minimum Size**: 400x400 pixels
- **Recommended Size**: 600x600 pixels or larger
- **Background**: Transparent or white
- **Color Space**: RGB or RGBA
- **File Size**: Under 500KB per image

### How to Convert/Optimize
If your downloaded images need optimization:
1. **Online Tools**: tinypng.com, compressorpng.com
2. **Desktop Software**: ImageMagick, GIMP (free)
3. **Windows**: Built-in Paint can resize

---

## Folder Structure After Download

After placing all images, your folder should look like:

```
Furniture-Visualizer/
└── src/main/resources/
    └── images/
        ├── wooden_chair.png          ← Downloaded
        ├── office_chair.png          ← Downloaded
        ├── dining_table.png          ← Downloaded
        ├── coffee_table.png          ← Downloaded
        ├── sofa.png                  ← Downloaded
        ├── bed.png                   ← Downloaded
        ├── desk.png                  ← Downloaded
        ├── bookshelf.png             ← Downloaded
        ├── lamp.png                  ← Downloaded
        └── (Optional thumbnails for faster loading)
```

---

## How Images Are Used in Your App

### In FurnitureLibraryPanel.java
```java
// Thumbnail image in list (50x50px, auto-scaled)
ImageIcon icon = new ImageIcon(url);
Image image = icon.getImage().getScaledInstance(50, 50, Image.SCALE_SMOOTH);
setIcon(new ImageIcon(image));
```

### In PropertiesPanel.java
```java
// Full preview image on right panel (220x220px)
imageLabel.setPreferredSize(new Dimension(220, 220));
```

The system automatically:
- Loads full-size image from PNG file
- Scales to 50x50px for list thumbnail
- Scales to 220x220px for preview panel
- Shows placeholder if image not found

---

## Troubleshooting

### Images Not Showing?
1. Check filename matches exactly (case-sensitive on some systems)
2. Verify file is in correct folder: `src/main/resources/images/`
3. Make sure file is actually PNG format
4. Rebuild project (Ctrl+F9)
5. Restart application

### Image Quality Issues?
1. Download higher resolution images (600x600px or larger)
2. Avoid JPEG format (use PNG only)
3. Remove compression if image is too small

### Where's My Folder?
Windows Path:
```
C:\Users\lap.lk\Desktop\Furniture-Visualizer\src\main\resources\images\
```

Or navigate via File Explorer:
- Desktop → Furniture-Visualizer
- src → main → resources → images

---

## ADVANCED: Adding More Furniture Later

If you want to add more furniture items later:

1. **Add to FurnitureDefaults.java:**
```java
items.add(new FurnitureItem(
    "Armchair",                    // Name
    FurnitureType.CHAIR,          // Type
    "/images/armchair.png",       // Image path (PNG)
    0.7, 0.85, 0.75,             // Width, Height, Depth
    "Fabric",                      // Material
    "Comfortable armchair."        // Description
));
```

2. **Download image and save as:** `armchair.png`

3. **Place in:** `src/main/resources/images/armchair.png`

4. **Rebuild and test!**

---

## Summary

✅ **Folder for images:** `C:\Users\lap.lk\Desktop\Furniture-Visualizer\src\main\resources\images\`

✅ **Format required:** PNG only

✅ **Images needed:** 9 (wooden_chair, office_chair, dining_table, coffee_table, sofa, bed, desk, bookshelf, lamp)

✅ **Minimum size:** 400x400px

✅ **Recommended size:** 600x600px or larger

✅ **Download from:** Unsplash, Pexels, Pixabay, or Freepik

✅ **After download:** Rebuild project and run to test

---

**Ready to download? Start with Unsplash.com and search for each furniture item!** 📥

