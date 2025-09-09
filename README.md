# businesscaseHB

# Subtree

## Update subtree from original repo

git fetch frontend-repo
git subtree pull --prefix=frontend frontend-repo main
git fetch backend-repo
git subtree pull --prefix=backend backend-repo main

## Push from subtree to original repo

git subtree push --prefix=frontend frontend-repo main
git subtree push --prefix=backend backend-repo main
