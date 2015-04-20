# Blakestone
Move over JSON

Blakestone is a new object notation system that incorporates
simple notation, quick hand feature implementation, and text level syntactic sugar

Blakestone will be encapsulated outside an IDE or interpreter with the .bs extension

Main features:
* Key-value notation
* Tab delimited indexing to represent children of an element
* Array indexes implied by lack of a key

Template Prototyping built in
* Create an element(s) based off of a given template
* Can provide ranges of values or use default ranges for primitives
* Can use regex to provide constraints for generated String data
* Can have a template repeat to create an array of elements using that template

# Basic Notation
    key:
        "value"     # Comment for index 0
        "value2"    # Comment for index 1
        1           # Comment for index 2
        0.5         # Comment for index 3
        newKey: "A single value"
        newArray:
            "An array in the parent"
            "Simply keep tabbing"
            "To indicate children"
        "And go back one"
        "To be back in parent"
        : 120 # A value with no key before the colon defaults to the index
        : # An array at index 7
            "Value in array"
            
# Template Notation
Template Repetition
* %t{3} # Indicates that the elements that are children to this should be
repeated three times and an array of the results made
* %t{1,3} # Will make 1 to 3 copies of the following template

Integers
* %d[X,Y,Z] # Selection
* %d[N-M] # Range, equivalent to %d[N, N+1, ..., M-1, M]
* %d[X, Y, N-M, A, B] # Combination
* %ld for longs

Floating Points
* %f[X,Y,Z] # Selection
* %f[N-M] # Range, using default precision of 2. 
EX: %f[0-1] can give values from 0.00 to 1.00
* %f.6[N-M] # Range with precision of 6
* %f[X,Y,N-M,A,B] # Combination

Strings
* %s[strA,strB,strC] # Selection, quotation marks assumed
* %s["strA,strB",strC] # Selection, use quotes to use comma in word
* %s[\"strA\",strB,strC] # Selection, escape quotes to include in string
* %s/[0-9]{3}[0-9]{3}[0-9]{4}/ # Regex string, use forward slashes to indicate

Regex Templates
* [] # Selection, will select one from range and/or letters inside
* () # Selection, will select one of the words
* {} # Repetition, will repeat previous choice the specified number of times