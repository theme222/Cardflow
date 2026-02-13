# How to add to resources

Each folder contains information on a level based on the name

## level.tsv
Tab separated values.
*A tsv file containing information on the level design. 
Please look [here](https://docs.google.com/spreadsheets/d/1YyVf_IwWB_XYJB_9f7PEmbEgjVSqCoR7jnTbRngjlos/edit?usp=sharing) 
for more details*

## config.json

Example layout looks like this

```json
{
  "name": "Example level name",
  "width": 9,
  "height": 9,

  "inputCards": [
    { "suit": "HEART", "value": 7, "material": "PLASTIC" },
    { "suit": "DIAMOND", "value": 8, "material": "PLASTIC" },
    { "suit": "HEART", "value": 9, "material": "STONE" },
    { "suit": "HEART", "value": 10, "material": "METAL" },
    { "suit": "SPADE", "value": 11, "material": "CORRUPTED" }
  ],

  "outputCards": [
    { "suit": "HEART", "value": 8},
    { "suit": "SPADE", "value": 11}
  ],

  "availableMovers": [
    { "name": "CONVEYOR", "count":  -1},
    { "name": "FLIPFLOP", "count":  2}
  ]
}
```



