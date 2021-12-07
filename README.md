# Advent Of Code

Advent of code event repository: this is the repository where I solve the problems in advent of code website, that come each year in December. It is like an advent calendar, but for programmers. The url is [https://adventofcode.com/](https://adventofcode.com/).

Each branch corresponds to a different year. The naming should point you to the right branch. The branch `main` is synchronized to the current year, but may be a bit late, so check in priority the branch named `adv-<year>`


### Side note
Below is because I recently renamed the `master` branch to `main`, and these are the instructions for updating the branch locally too.

```shell
git branch -m master main
git fetch origin
git branch -u origin/main main
git remote set-head origin -a
```
