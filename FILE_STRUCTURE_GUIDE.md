# 📂 FILE STRUCTURE & PLACEMENT GUIDE

## Quick Answer: WHERE TO PUT IMAGES

```
C:\Users\lap.lk\Desktop\Furniture-Visualizer\src\main\resources\images\
```

Copy all your downloaded PNG files here. That's it!

---

## Full Project Structure

```
C:\Users\lap.lk\Desktop\Furniture-Visualizer\
│
├── src\
│   └── main\
│       ├── java\
│       │   └── com\teamname\furniviz\
│       │       ├── app\              ← Navigation, Main Frame
│       │       ├── furniture\        ← YOUR MODULE (FurnitureLibraryPanel)
│       │       ├── room\             ← Room System
│       │       ├── auth\             ← Login
│       │       └── ...
│       │
│       └── resources\
│           └── images\               ← ⭐ IMAGES GO HERE ⭐
│               ├── bed.png
│               ├── bed_thumb.png
│               ├── bookshelf.png
│               ├── bookshelf_thumb.png
│               ├── coffee_table.png
│               ├── coffee_table_thumb.png
│               ├── desk.png
│               ├── desk_thumb.png
│               ├── dining_table.png
│               ├── dining_table_thumb.png
│               ├── lamp.png
│               ├── lamp_thumb.png
│               ├── office_chair.png
│               ├── office_chair_thumb.png
│               ├── sofa.png
│               ├── sofa_thumb.png
│               ├── wooden_chair.png
│               └── wooden_chair_thumb.png
│
├── target\                           ← Compiled classes (auto-generated)
├── lib\                              ← External JAR files
├── pom.xml                           ← Maven config
└── ...
```

---

## Images Folder - Detailed View

### Current Content (18 files)

```
📁 images\
│
├── 📄 bed.png                    (Full preview - 220x220 display)
├── 📄 bed_thumb.png             (Thumbnail - 50x50 list display)
│
├── 📄 bookshelf.png             (Full preview)
├── 📄 bookshelf_thumb.png       (Thumbnail)
│
├── 📄 coffee_table.png          (Full preview)
├── 📄 coffee_table_thumb.png    (Thumbnail)
│
├── 📄 desk.png                  (Full preview)
├── 📄 desk_thumb.png            (Thumbnail)
│
├── 📄 dining_table.png          (Full preview)
├── 📄 dining_table_thumb.png    (Thumbnail)
│
├── 📄 lamp.png                  (Full preview)
├── 📄 lamp_thumb.png            (Thumbnail)
│
├── 📄 office_chair.png          (Full preview)
├── 📄 office_chair_thumb.png    (Thumbnail)
│
├── 📄 sofa.png                  (Full preview)
├── 📄 sofa_thumb.png            (Thumbnail)
│
├── 📄 wooden_chair.png          (Full preview)
└── 📄 wooden_chair_thumb.png    (Thumbnail)
```

### Image Pairing System

Each furniture item has TWO images:

| Full Image | Thumbnail Image | Used Where |
|---|---|---|
| `bed.png` | `bed_thumb.png` | List (50x50), Preview (220x220) |
| `bookshelf.png` | `bookshelf_thumb.png` | List (50x50), Preview (220x220) |
| `coffee_table.png` | `coffee_table_thumb.png` | List (50x50), Preview (220x220) |
| `desk.png` | `desk_thumb.png` | List (50x50), Preview (220x220) |
| `dining_table.png` | `dining_table_thumb.png` | List (50x50), Preview (220x220) |
| `lamp.png` | `lamp_thumb.png` | List (50x50), Preview (220x220) |
| `office_chair.png` | `office_chair_thumb.png` | List (50x50), Preview (220x220) |
| `sofa.png` | `sofa_thumb.png` | List (50x50), Preview (220x220) |
| `wooden_chair.png` | `wooden_chair_thumb.png` | List (50x50), Preview (220x220) |

---

## How Images Are Referenced

### In Code (FurnitureDefaults.java)

```java
items.add(new FurnitureItem(
    "Wooden Chair",                    // Display name
    FurnitureType.CHAIR,              // Type
    "/images/wooden_chair.png",       // ← Path to full image
    0.5, 0.9, 0.5,                    // Dimensions
    "Wood",                            // Material
    "A classic wooden chair."          // Description
));
```

The `/images/wooden_chair.png` path refers to:
```
src/main/resources/images/wooden_chair.png
```

### At Runtime

When the application runs:
1. Java reads the image from `src/main/resources/images/`
2. Scales to 50x50px for list display
3. Scales to 220x220px for preview display
4. Auto-generates thumbnails if needed

---

## Three Ways to Access the Folder

### Method 1: Windows Explorer (Easiest)
```
1. Open Windows Explorer
2. Copy this path: C:\Users\lap.lk\Desktop\Furniture-Visualizer\src\main\resources\images\
3. Paste in address bar
4. Press Enter
```

### Method 2: From IntelliJ IDEA
```
1. Open IntelliJ IDEA
2. In Project view on left
3. Expand: Furniture-Visualizer
4. Expand: src
5. Expand: main
6. Expand: resources
7. Right-click "images" folder
8. Click "Show in Explorer"
```

### Method 3: From Command Line
```powershell
cd "C:\Users\lap.lk\Desktop\Furniture-Visualizer\src\main\resources\images"
dir
```

---

## Download & Copy Process

### Step 1: Download
```
From Unsplash/Pexels/Pixabay:
1. Find image
2. Click Download
3. Save to your Downloads folder
4. Rename to correct name (wooden_chair.png, etc.)
```

### Step 2: Copy to Project
```
1. Open Windows Explorer
2. Navigate to Downloads folder
3. Right-click your image file
4. Click "Copy"
5. Navigate to: C:\Users\lap.lk\Desktop\Furniture-Visualizer\src\main\resources\images\
6. Right-click in empty space
7. Click "Paste"
```

---

## Common Mistakes to Avoid

### ❌ Wrong Folder Path
```
WRONG:
C:\Users\lap.lk\Desktop\Furniture-Visualizer\
C:\Users\lap.lk\Desktop\Furniture-Visualizer\src\
C:\Users\lap.lk\Desktop\Furniture-Visualizer\target\

CORRECT:
C:\Users\lap.lk\Desktop\Furniture-Visualizer\src\main\resources\images\
```

### ❌ Wrong Filenames
```
WRONG:
Wooden_Chair.png          (Capital letters)
wooden-chair.png          (Dash instead of underscore)
wooden chair.png          (Space instead of underscore)
WOODEN_CHAIR.PNG          (Different case)

CORRECT:
wooden_chair.png          (Lowercase, underscores)
office_chair.png
dining_table.png
```

### ❌ Wrong File Format
```
WRONG:
wooden_chair.jpg          (JPEG format)
wooden_chair.jpeg
wooden_chair.gif
wooden_chair.bmp

CORRECT:
wooden_chair.png          (PNG format ONLY)
```

### ❌ Wrong Image Size
```
WRONG:
- Images under 300px
- Very low quality
- Blurry or pixelated
- Just text/icons

CORRECT:
- 400x400px minimum
- 600x600px recommended
- Clear and sharp
- Professional quality
```

---

## Verification Checklist

After downloading all images:

### ✓ Check Folder Contents
```
Navigate to: C:\Users\lap.lk\Desktop\Furniture-Visualizer\src\main\resources\images\

Verify you see:
☑ bed.png                      (Newly downloaded)
☑ bed_thumb.png                (Existing - keep it)
☑ bookshelf.png                (Newly downloaded)
☑ bookshelf_thumb.png          (Existing - keep it)
☑ coffee_table.png             (Newly downloaded)
☑ coffee_table_thumb.png       (Existing - keep it)
☑ desk.png                     (Newly downloaded)
☑ desk_thumb.png               (Existing - keep it)
☑ dining_table.png             (Newly downloaded)
☑ dining_table_thumb.png       (Existing - keep it)
☑ lamp.png                     (Newly downloaded)
☑ lamp_thumb.png               (Existing - keep it)
☑ office_chair.png             (Newly downloaded)
☑ office_chair_thumb.png       (Existing - keep it)
☑ sofa.png                     (Newly downloaded)
☑ sofa_thumb.png               (Existing - keep it)
☑ wooden_chair.png             (Newly downloaded)
☑ wooden_chair_thumb.png       (Existing - keep it)

Total: 18 files (9 downloaded + 9 existing)
```

### ✓ Check File Sizes
```
In Windows Explorer:
1. Click View menu
2. Click Details
3. Check Size column

Each image should be:
- Minimum: 50 KB
- Normal: 200-500 KB
- Maximum: 1000 KB (1 MB)

If under 50 KB → Image too small
If over 1 MB → May need compression
```

### ✓ Check File Properties
```
Right-click a PNG file → Properties

Details should show:
- Type: PNG Image
- Size: 200 KB - 500 KB (typical)
- Dimensions: 400x400px or larger

NOT:
- Type: JPEG Image, BMP, etc.
```

---

## After Verification - Next Steps

### 1. Rebuild Project
```
IntelliJ IDEA:
- Click Build menu
- Click Rebuild Project
- Or press Ctrl+F9
```

### 2. Run Application
```
IntelliJ IDEA:
- Click Run menu
- Click Run App
- Or press Shift+F10
```

### 3. Test Images
```
In Application:
1. Click "Furniture Library" button
2. Verify list shows 9 items with thumbnails
3. Click each item
4. Verify preview image appears on right panel
5. All 9 items should show images
```

---

## Need to Add More Furniture Later?

To add a new furniture item:

### 1. Create Image Files
```
Download two images:
- full_size_image.png (400x400px or larger)
- full_size_image_thumb.png (50x50px or keep full-size)

Save both to: C:\Users\lap.lk\Desktop\Furniture-Visualizer\src\main\resources\images\
```

### 2. Update Code
```java
// In FurnitureDefaults.java
items.add(new FurnitureItem(
    "Item Name",
    FurnitureType.CATEGORY,
    "/images/item_name.png",    // ← Must match filename
    width, height, depth,
    "Material",
    "Description"
));
```

### 3. Rebuild & Test
```
Rebuild project → Run app → Test
```

---

## Summary Quick Reference

| Question | Answer |
|---|---|
| **Where to put images?** | `C:\Users\lap.lk\Desktop\Furniture-Visualizer\src\main\resources\images\` |
| **What format?** | PNG only (not JPEG, GIF, BMP) |
| **How many images?** | 9 full-size + 9 thumbnails = 18 total |
| **Filenames?** | Exact match: `wooden_chair.png`, `office_chair.png`, etc. |
| **Minimum size?** | 400x400 pixels |
| **Recommended size?** | 600x600 pixels or larger |
| **File size per image?** | 200-500 KB typical |
| **Where to download?** | Unsplash.com, Pexels.com, Pixabay.com |
| **After download?** | Rebuild project (Ctrl+F9) and test |

---

✅ **Ready to download and organize your furniture images!**

