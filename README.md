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