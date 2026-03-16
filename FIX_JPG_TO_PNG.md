# 🔄 FIX: Convert JPG to PNG

## Current Status

You have **mixed file formats** in your images folder:

```
✓ PNG files (correct):
  - bed.png
  - coffee_table.png
  - sofa.png

✗ JPG files (need to convert):
  - bookshelf.jpg
  - desk.jpg
  - dining_table.jpg
  - lamp.jpg
  - office_chair.jpg
```

**Total:** 5 JPG files that need conversion to PNG

---

## Why This Matters

Your system is configured for **PNG ONLY** because:
- ✅ PNG has better transparency support
- ✅ PNG is lossless (no quality loss)
- ✅ Java Swing handles PNG better
- ✅ Consistent with project architecture

**JPG will cause display issues in the app!**

---

## How to Fix (3 Easy Options)

### Option 1: Online Converter (Easiest - No Software Needed) ⭐

**Use CloudConvert.com or Convertio.co:**

1. Go to **cloudconvert.com**
2. Click "Select Files"
3. Select a JPG file from your images folder
4. Choose format: **PNG**
5. Click Convert
6. Download the PNG file
7. Replace the original JPG in your folder

**Repeat for all 5 JPG files:**
- bookshelf.jpg → bookshelf.png
- desk.jpg → desk.png
- dining_table.jpg → dining_table.png
- lamp.jpg → lamp.png
- office_chair.jpg → office_chair.png

**Time:** 5-10 minutes

---

### Option 2: Windows Paint (Built-in) ⭐

**Use Windows Paint (free, already installed):**

1. Right-click JPG file
2. Click "Open with" → "Paint"
3. File → "Save as"
4. Change format from "JPEG" to "PNG"
5. Keep filename, add .png extension
6. Click Save
7. Delete original JPG

**Repeat for all 5 JPG files**

**Time:** 10 minutes total

---

### Option 3: Python Script (If you have Python)

**If you have Python installed:**

```bash
cd C:\Users\lap.lk\Desktop\Furniture-Visualizer
python convert_jpg_to_png.py
```

The script will:
- Automatically convert all JPG files to PNG
- Delete original JPG files
- Show results

**Time:** 1 minute

---

### Option 4: ImageMagick (If you have it)

**If you have ImageMagick installed:**

```bash
cd "C:\Users\lap.lk\Desktop\Furniture-Visualizer\src\main\resources\images"

mogrify -format png *.jpg
del *.jpg
```

**Time:** 30 seconds

---

## Recommended: Option 1 (CloudConvert)

**Why:**
- No software installation needed
- Works in browser
- Very fast
- Reliable
- Free

**Steps:**
1. Go to cloudconvert.com
2. Upload each JPG
3. Convert to PNG
4. Download and save to your images folder
5. Done!

---

## After Converting All Files

### Step 1: Verify All PNG Files

Open Windows Explorer:
```
C:\Users\lap.lk\Desktop\Furniture-Visualizer\src\main\resources\images\
```

You should see **9 PNG files** (all lowercase):
```
✓ bed.png
✓ bookshelf.png (converted from JPG)
✓ coffee_table.png
✓ desk.png (converted from JPG)
✓ dining_table.png (converted from JPG)
✓ lamp.png (converted from JPG)
✓ office_chair.png (converted from JPG)
✓ sofa.png
✓ wooden_chair.png (if you have it)
```

**NO JPG FILES SHOULD REMAIN**

### Step 2: Rebuild Project

In IntelliJ IDEA:
```
Build → Rebuild Project
Or: Ctrl+F9
```

### Step 3: Run and Test

```
Run → Run App
Click "Furniture Library"
Verify all images display correctly
```

---

## Troubleshooting

### "Files won't convert"
- Use online tool: cloudconvert.com
- Works 100% of the time
- No installation needed

### "I accidentally deleted a file"
- Re-download from Unsplash or Pexels
- Convert to PNG
- Place back in folder

### "After conversion, images still don't show"
1. Rebuild project (Ctrl+F9)
2. Restart app
3. Try again

---

## File Format Comparison

| Format | Transparency | Size | Quality | Java Support |
|--------|-------------|------|---------|---|
| PNG | ✅ Yes | Medium | Lossless | ✅ Perfect |
| JPG | ❌ No | Smaller | Lossy | ⚠️ Issues |
| GIF | ✅ Limited | Small | Limited | ✅ OK |
| BMP | ✅ Yes | Large | Lossless | ⚠️ Slow |

**PNG is the best choice for this project!**

---

## Summary

**Current Issue:**
- 5 JPG files mixed with PNG files
- System requires PNG only

**Solution:**
- Convert all 5 JPG files to PNG
- Use online tool (easiest option)
- Takes 10 minutes

**Result:**
- All 9 PNG files properly organized
- Furniture Library works perfectly
- Ready for production

---

## Next Steps

1. **Choose conversion method** (Option 1 recommended)
2. **Convert all 5 JPG files** to PNG
3. **Verify all files are PNG** in Windows Explorer
4. **Rebuild project** (Ctrl+F9)
5. **Test app** - Furniture Library should work perfectly

---

**Recommended:** Use CloudConvert.com - takes 5 minutes, zero hassle! 🚀

