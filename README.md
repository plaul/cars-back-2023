# Info

### Initial setup for most projects used this semester

Many of the solutions to individual weeks, can be found in a branch for the given week
This branch represents what we did together in the class, replace xxxx below with week2-crud to get your OWN copy starting from this branch


## How to get YOUR OWN Cars 'R' Us code, updated to a given week
If you wan't to jump into a certain week, in order to be able to complete "next weeks" tasks/exercises, do the following in  **Git Bash**
or if you are using a Mac, your own mac-terminal.

#### First open a terminal in the folder where you want to create the new project.
Then, in the terminal, do the following:

```

# Clone the xxxx branch (here illustrated with week1) from the remote repostiory
# Important: REPLACE week1 below, with the branch you wan't ot clone
git clone -b week1 https://github.com/kea-spring2023/cars-r-us.git

#Verify that a cars-r-us folder was created
ls
# navigate into this folder
cd cars-r-us
# Verify that this folder has a git-repo folder (.git)
ls -a
# Delete this folder
rm -rf .git
# Verify that it's gone
ls -a
# Set the project under version control again, this time without reference to MY repository
git init
# Verify that you have a git-folder (.git) again
ls -a
# Set main branch name to "main"
git branch -m main
# Add everything in the project folder to the staging area
git add .
# Commit, wiht a message that makes sense
git commit -m "ready for week1"
# Now create your own cars repo on github, and do what your normally do to push this project up here
# Finally open the project in your IDE
```