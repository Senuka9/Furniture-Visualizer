#!/usr/bin/env python3
"""
Convert JPG images to PNG in the images folder
This script uses PIL/Pillow library to convert JPG files to PNG
"""

import os
from pathlib import Path

def convert_jpg_to_png():
    images_folder = r"C:\Users\lap.lk\Desktop\Furniture-Visualizer\src\main\resources\images"

    print("=" * 50)
    print("JPG to PNG Converter")
    print("=" * 50)
    print()

    # Check if PIL is available
    try:
        from PIL import Image
        print("[OK] PIL/Pillow library found")
    except ImportError:
        print("[ERROR] PIL/Pillow not installed")
        print()
        print("To install, run:")
        print("  pip install Pillow")
        print()
        return False

    print()
    print(f"Converting images in: {images_folder}")
    print()

    # Find all JPG files
    jpg_files = list(Path(images_folder).glob("*.jpg"))
    jpg_files_lower = list(Path(images_folder).glob("*.JPG"))
    jpg_files += jpg_files_lower

    if not jpg_files:
        print("[INFO] No JPG files found!")
        return True

    print(f"Found {len(jpg_files)} JPG files to convert:")
    print()

    # Convert each JPG to PNG
    converted = 0
    for jpg_file in jpg_files:
        try:
            png_filename = jpg_file.stem + ".png"
            png_path = jpg_file.parent / png_filename

            # Open JPG and convert to RGB (removes alpha if exists)
            img = Image.open(jpg_file)

            # Convert RGBA or other formats to RGB for JPG compatibility
            if img.mode != 'RGB':
                img = img.convert('RGB')

            # Save as PNG
            img.save(png_path, 'PNG')

            print(f"  ✓ {jpg_file.name} → {png_filename}")
            converted += 1

            # Delete original JPG
            jpg_file.unlink()
            print(f"    (removed original JPG)")

        except Exception as e:
            print(f"  ✗ {jpg_file.name} - ERROR: {str(e)}")

    print()
    print("=" * 50)
    print(f"Conversion Complete: {converted}/{len(jpg_files)} files converted")
    print("=" * 50)
    print()

    # List final contents
    print("Final folder contents:")
    print()
    for file in sorted(Path(images_folder).glob("*")):
        print(f"  {file.name}")

    print()
    print("[NEXT STEPS]")
    print("1. Rebuild project (Ctrl+F9 in IntelliJ)")
    print("2. Run application")
    print("3. Test Furniture Library")
    print()

    return True

if __name__ == "__main__":
    convert_jpg_to_png()
    input("Press Enter to exit...")

