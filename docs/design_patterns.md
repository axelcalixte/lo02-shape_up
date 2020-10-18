# Visitor pattern

Should be used to count scores : doesn't seem better than having a
`countScores` method on the `Board` interface.

Could be useful if we implemented several score counting methods (several
concrete visitors).

# Strategy pattern

A variation of it will be used for the AI : there will be something like an
AI interface with several methods that will be called by the GameController
when necessary.

Could also be used to implement rule variations.

# Observer pattern

Will be used to notify the UI of game state changes.

