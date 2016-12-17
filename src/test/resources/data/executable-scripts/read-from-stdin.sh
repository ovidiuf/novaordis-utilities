#!/bin/bash

echo "writes to stdout first"
echo "attempting to read from stdin ..."

read a

echo "read line: ${a}"
