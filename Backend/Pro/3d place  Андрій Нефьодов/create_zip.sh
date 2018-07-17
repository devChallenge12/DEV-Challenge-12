#!/usr/bin/env bash
rm $1.zip
zip -r $1.zip ./src/* *.* -x "*.zip" -x "*downloaded.md"