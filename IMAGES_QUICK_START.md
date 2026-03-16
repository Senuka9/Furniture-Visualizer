# 🎯 FURNITURE IMAGE SETUP - QUICK START

## Your Question: Where to put furniture images?

### ✅ ANSWER: 
```
C:\Users\lap.lk\Desktop\Furniture-Visualizer\src\main\resources\images\
```

**That's it! Copy all PNG images to this folder.**

---

## What You Need to Download

### 9 Furniture Images Required

You need to download **ONE image for each furniture item** and save as PNG:

| # | Furniture Item | Save As Filename |
|---|---|---|
| 1 | Wooden Chair | `wooden_chair.png` |
| 2 | Office Chair | `office_chair.png` |
| 3 | Dining Table | `dining_table.png` |
| 4 | Coffee Table | `coffee_table.png` |
| 5 | 3-Seater Sofa | `sofa.png` |
| 6 | Queen Bed | `bed.png` |
| 7 | Office Desk | `desk.png` |
| 8 | Bookshelf | `bookshelf.png` |
| 9 | Table Lamp | `lamp.png` |

---

## How to Download (3 Simple Steps)

### Step 1: Go to Unsplash.com
```
1. Open browser
2. Go to unsplash.com
3. Search for: "wooden chair white background"
4. Find a good image
5. Click Download
```

### Step 2: Rename the File
```
Your download will be named something like:
  unsplash_abc123.png

Rename it to:
  wooden_chair.png

(Right-click → Rename → Type new name)
```

### Step 3: Move to Project Folder
```
Copy the file to:
  C:\Users\lap.lk\Desktop\Furniture-Visualizer\src\main\resources\images\

(Drag & drop or copy-paste)
```

**Repeat for all 9 items.**

---

## Download Quick Checklist

```
Search Terms for Unsplash:
☐ wooden chair white background
☐ office chair white background
☐ dining table white background
☐ coffee table white background
☐ sofa white background
☐ bed white background
☐ desk white background
☐ bookshelf white background
☐ lamp white background

After Download:
☐ Rename to: wooden_chair.png
☐ Rename to: office_chair.png
☐ Rename to: dining_table.png
☐ Rename to: coffee_table.png
☐ Rename to: sofa.png
☐ Rename to: bed.png
☐ Rename to: desk.png
☐ Rename to: bookshelf.png
☐ Rename to: lamp.png

Copy All To:
☐ C:\Users\lap.lk\Desktop\Furniture-Visualizer\src\main\resources\images\
```

---

## After Download: Verify Everything

### Open Folder in Windows Explorer

Navigate to:
```
C:\Users\lap.lk\Desktop\Furniture-Visualizer\src\main\resources\images\
```

You should see **18 files**:
```
✓ bed.png + bed_thumb.png
✓ bookshelf.png + bookshelf_thumb.png
✓ coffee_table.png + coffee_table_thumb.png
✓ desk.png + desk_thumb.png
✓ dining_table.png + dining_table_thumb.png
✓ lamp.png + lamp_thumb.png
✓ office_chair.png + office_chair_thumb.png
✓ sofa.png + sofa_thumb.png
✓ wooden_chair.png + wooden_chair_thumb.png

Total: 18 PNG files
```

---

## Final Step: Rebuild & Test

### Rebuild Project
```
IntelliJ IDEA:
1. Click Build menu
2. Click Rebuild Project
3. Wait for completion
```

### Run Application
```
1. Click Run menu
2. Click Run App
3. App opens
```

### Test Furniture Library
```
1. Click "Furniture Library" button
2. See list of 9 items on left
3. Click each item
4. Verify preview image shows on right panel
5. All images should display correctly
```

---

## Image Requirements

### Format
- **Must be PNG** (not JPEG, GIF, or BMP)
- Transparent background is optional

### Size
- **Minimum**: 400x400 pixels
- **Recommended**: 600x600 pixels or larger
- **Maximum**: 1000 KB per file

### Quality
- Clear and sharp
- Professional product photo style
- White or clean background
- Front-facing view

---

## If Images Don't Show

### Troubleshooting

1. **Check folder path is correct:**
   ```
   C:\Users\lap.lk\Desktop\Furniture-Visualizer\src\main\resources\images\
   ```

2. **Check filenames match exactly:**
   ```
   ✓ wooden_chair.png (correct - lowercase, underscore)
   ✗ Wooden_Chair.png (wrong - uppercase)
   ✗ wooden-chair.png (wrong - dash instead of underscore)
   ```

3. **Check file format:**
   ```
   ✓ wooden_chair.png (PNG format)
   ✗ wooden_chair.jpg (JPEG format)
   ```

4. **Rebuild project:**
   ```
   IntelliJ: Ctrl+F9
   ```

5. **Restart application:**
   ```
   Close and run app again
   ```

---

## Free Image Download Sites

### Best for Product Photos
- **Unsplash** (unsplash.com) ⭐ Recommended
- **Pexels** (pexels.com)
- **Pixabay** (pixabay.com)

### For Illustrated Furniture
- **Freepik** (freepik.com)
- **Flaticon** (flaticon.com)

### AI-Generated (if needed)
- **DALL-E** (openai.com/dall-e)
- **Midjourney** (midjourney.com)

---

## What Happens After Download

When you place PNG images in the folder:

1. **List Display** (Left Panel)
   - Shows 50x50px thumbnail
   - Auto-scaled from full image

2. **Preview Display** (Right Panel)
   - Shows 220x220px preview
   - Auto-scaled from full image

3. **No Manual Thumbnails Needed**
   - System handles all scaling
   - Just provide full-size image

---

## Adding More Furniture Later

To add another furniture item:

1. **Download image** → Save as PNG to folder
2. **Edit FurnitureDefaults.java:**
   ```java
   items.add(new FurnitureItem(
       "New Furniture Name",
       FurnitureType.CATEGORY,
       "/images/new_furniture.png",
       width, height, depth,
       "Material",
       "Description"
   ));
   ```
3. **Rebuild & test**

---

## Summary

| Task | How | Folder |
|------|-----|--------|
| Download images | Unsplash.com | Downloads folder |
| Rename files | Right-click → Rename | Downloads folder |
| Copy to project | Drag & drop or copy-paste | `src/main/resources/images/` |
| Verify | Check 18 files exist | `src/main/resources/images/` |
| Rebuild | Build → Rebuild Project | IntelliJ |
| Test | Run app → Furniture Library | Application |

---

## Created Help Documents

I've created detailed guides in your project folder:

1. **FURNITURE_IMAGE_DOWNLOAD_GUIDE.md** - Complete download guide
2. **IMAGE_DOWNLOAD_CHECKLIST.md** - Step-by-step checklist
3. **FILE_STRUCTURE_GUIDE.md** - File organization details

---

## You're All Set! 🚀

**Next Action:** Go to unsplash.com and start downloading furniture images!

**Then:** Copy them to `C:\Users\lap.lk\Desktop\Furniture-Visualizer\src\main\resources\images\`

**Finally:** Rebuild project and test in your app!

Questions? Check the detailed guides created in your project folder.

---

**Happy downloading!** 📥🛋️🪑🛏️

