Gentools
-----

This library allows you to create a Punnett square for polyhybrid crossing (mono- , di- and multiple crossings).

Example: Monohybrid crossing Punnett Square:


|   | A  | a  |
|---|----|----|
| A | AA | Aa |
| a | Aa | aa |

More information about Punnett Square: https://en.wikipedia.org/wiki/Punnett_square

### Basic Usage

For crossing simple sequences (for examples **AaBbCc** and **aaBBCCdd**) you need to do the following:

1. Create Crossing object with default Combiner. Combiner must contain default SymbolGameteGroupExtractor and SequenceValidation:

```java
    GameteGroupsExtractor extractor = new SymbolGameteGroupsExtractor();
    SequenceValidation validation = new SequenceValidation(extractor);
    GameteCombiner combiner = new GameteCombiner(validation, extractor);
    
    Crossing crossing = new PolyhybridCrossing(combiner);
```

2. Now you can get Punnett Square by passing two sequences to our Crossing class:

```java 
    Map<String, Integer> result = crossing.crossing("AaBbCc", "aaBBCc");
```

The result will be similar to this:

```
AaBbCc : 4
aaBBcc : 1
....
AAbbCC : 2
``` 
### Tokenizers

In general the Punnett square is generated based on two sequences
of the same `set of tokens`. Let's take a look at an example of polyhybrid
crossing of two sequences:

```java
crossing.crossing("AaBBcc", "aAbbCC");
```

In this example we have the following tokens: `a`, `b` and `c`, which
can be upper-cased. But if we want to have more meaningful set of tokens
like `prl`, `rn`, `cr` we can do this using [Tokenizers](src/main/java/com/purrer/gentools/interfaces/SequenceTokenizer.java).

### Example

Let's assume that we need a behavior of [PolyhybridCrossing](src/main/java/com/purrer/gentools/crossing/PolyhybridCrossing.java)
which will create a Punnett square for sequences with gametes `prl`, `rn`, `cr` and each gamete can be dominant 
(first letter is upper-cased) and recessive, for example

```
- `PrlprlRnRncrCr`
- `PrlPrlrnRncrcr`
```

To create your own behavior of [PolyhybridCrossing](src/main/java/com/purrer/gentools/crossing/PolyhybridCrossing.java)
with our own set of gametes let's first create our custom 
[Tokenizers](src/main/java/com/purrer/gentools/interfaces/SequenceTokenizer.java).

```java
import com.purrer.gentools.interfaces.Token;

import java.util.ArrayList;
import java.util.List;

public class GametesDependentTokenizer implements SequenceTokenizer {

    private final List<String> genes = List.of("Prl", "Rn", "Cr");

    @Override
    public List<Token> tokenize(String sequence) {
        List<Token> result = new ArrayList<>();

        for (String gene : genes) {
            for (int i = 0; i < 2; i++) {
                if (sequence.contains(gene)) {
                    result.add(new DefaultToken(gene));
                    sequence = sequence.replaceFirst(gene, "");
                }
            }

            String lowerGene = gene.toLowerCase();
            for (int i = 0; i < 2; i++) {
                if (sequence.contains(lowerGene)) {
                    result.add(new DefaultToken(lowerGene));
                    sequence = sequence.replaceFirst(lowerGene, "");
                }
            }
        }
        
        return result;
    }
}
```

we have to create implementations of 2 processing
entities:
- [SequenceValidation](src/main/java/com/purrer/gentools/interfaces/SequenceValidation.java) to validate input
sequences
- [GameteGroupsExtractor](src/main/java/com/purrer/gentools/interfaces/GameteGroupsExtractor.java) to extract the
gamete groups from input sequences.

Now we can use our custom `Tokenizer` to create an instance of `GameteGroupExtractor` and `SequenceValidation`

```java
import com.purrer.gentools.crossing.PolyhybridCrossing;
import com.purrer.gentools.interfaces.SequenceValidation;
import com.purrer.gentools.validation.SequenceValidationImpl;

class PolyhybridCrossingConfig {
    public PolyhybridCrossing createCrossing() {
        SequenceTokenizer tokenizer = new GametesDependentTokenizer();
        GameteGroupsExtractor extractor = new TokenizingGameteGroupsExtractor(tokenizer);
        SequenceValidation validation = new SequenceValidationImpl(extractor);
        ...
    }
}
```

And finally we can create the instance of a [GameteCombiner](src/main/java/com/purrer/gentools/utils/GameteCombiner.java)
and [PolyhybridCrossing](src/main/java/com/purrer/gentools/crossing/PolyhybridCrossing.java)

```java
import com.purrer.gentools.crossing.PolyhybridCrossing;
import com.purrer.gentools.interfaces.SequenceValidation;
import com.purrer.gentools.utils.GameteCombiner;
import com.purrer.gentools.validation.SequenceValidationImpl;

class PolyhybridCrossingConfig {
    public PolyhybridCrossing createCrossing() {
        SequenceTokenizer tokenizer = new GametesDependentTokenizer();
        
        GameteGroupsExtractor extractor = new TokenizingGameteGroupsExtractor(tokenizer);
        SequenceValidation validation = new SequenceValidationImpl(extractor);
        
        GameteCombiner combiner = new GameteCombiner(validation, extractor);
        return new PolyhybridCrossing(combiner);
    }
}
```

