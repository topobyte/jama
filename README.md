# jama

[Jama](http://math.nist.gov/javanumerics/jama/) is a basic linear algebra
package for Java originally developed by
[MathWorks](https://www.mathworks.com)
and [NIST](https://www.nist.gov) and released to the public domain.

This is the original copyright statement:

> This software is a cooperative product of The MathWorks and
> the National Institute of Standards and Technology (NIST) which has
> been released to the public domain. Neither The MathWorks nor NIST assumes
> any responsibility whatsoever for its use by other parties, and makes no
> guarantees, expressed or implied, about its quality, reliability, or any
> other characteristic.

## Why this version?

The original Jama artifact available through Maven
(`gov.nist.math:jama:1.0.3`) has a few deficiencies:
* The package name is `Jama`, which does not follow Java naming conventions
  because it is not lowercase.
* It contains both example and testing code which does not belong into a
  production release.
* Some I/O code in the Matrix class does not work well with transpilation
  frameworks such as GWT and JSweet.

## Changelog

For older changes, see the
[original changelog](https://github.com/topobyte/jama/blob/master/ChangeLog.md).

* Split source code to main and test (includes example)
* Formatted source code using Eclipse formatter
* Preserved some well formatted comments using `@formatter:on/off` annotations
* Added missing curly braces and `@Override` annotations
* Removed unnecessary casts
* Move I/O methods (`print()` variants and `read()`) from `Matrix` to new
  class `MatrixIO`.
