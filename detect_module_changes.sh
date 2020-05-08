#!/bin/bash

# MIT License
#
# Copyright (c) 2020
#
# Permission is hereby granted, free of charge, to any person obtaining a copy of this software and
# associated documentation files (the "Software"), to deal in the Software without restriction,
# including without limitation the rights to use, copy, modify, merge, publish, distribute,
# sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is
# furnished to do so, subject to the following conditions:
#
# The above copyright notice and this permission notice shall be included in all copies or
# substantial portions of the Software.
#
# THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED,
# INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE
# AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM,
# DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
# OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.

# Exit on error
set -e

# unshallow since GitHub actions does a shallow clone
git fetch --unshallow
git fetch origin

command=$(git diff --name-only origin/master)
# Get all the modules that were changed
while read -r line; do
  module_name="${line%%/*}"
  if [[ ${MODULES} != *"${module_name}"* ]]; then
    MODULES="${MODULES} ${module_name}"
  fi
done <<< "$command"
changed_modules=$MODULES

echo "Found changes in $changed_modules"
# Get a list of all available gradle tasks
AVAILABLE_TASKS=$(./gradlew tasks --all)

# Check if these modules have gradle tasks
build_commands=""
for module in $changed_modules
do
  if [[ $AVAILABLE_TASKS =~ $module":" ]]; then
    build_commands=${build_commands}" :"${module}":jacocoTestDebugUnitTestReport"
  fi
done

if [[ $build_commands == "" ]]; then
    echo "Skip unit tests...."
fi

# Pass the commands to next step
echo "::set-env name=build_commands::$build_commands"
exit 0