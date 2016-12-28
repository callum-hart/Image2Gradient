# Image2Gradient
Replicating images with CSS gradients

### About

### Examples

Pictures *ahem* gradients speak a thousand words...

### Usage

Download & navigate to class

```
$ git clone git@github.com:callum-hart/Image2Gradient.git
$ cd out/production/
```

And run :runner:
 
 `$ Image2Gradient [-optional_args] <path-2-Image>`
 
 ### CLI
 
| Flag | Description | Value | Default
| :---| :--- | :--- | :--- |
| **`-t`** <br> *gradient type* | Type of linear gradient | <table><tr><td>t2b</td><td>*top to bottom*</td></tr><tr><td>l2r</td><td>*left to right*</td></tr><tr><td>bl2tr</td><td>*bottom left to top right*</td></tr><tr><td>br2tl</td><td>*bottom right to top left*</td></tr></table> | t2b |
| **`-f`** <br> *fidelity* | Level of precision <br>*(higher number = more accurate gradient)* | Integer greater than or equal to 2 | 10 |
| **`-v`** <br> *vendors* | Browsers to support | <table><tr><td>web</td><td>*Webkit*</td></tr><tr><td>moz</td><td>*Firefox*</td></tr><tr><td>opera</td><td>*Opera*</td></tr></table> | All |
| **`--help`** | Print CLI usage | n/a | n/a |

### Use case~~s~~

I'd say this is more experimental than useful :cry:

HOWEVER...

CSS gradients can provide fallbacks to images *(whilst they load)* as discussed here, here and here. 

...Though rendering a low resolution image first *(rather than a gradient)* is probably more effective :sob:

### Resources


 