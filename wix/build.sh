#!/bin/bash

set -e

echo "Building MSI using WIX"
candle Files.wxs Product.wxs -dPlatform=x64 -arch x64
light Files.wixobj Product.wixobj -ext WixUIExtension -out Product.msi
