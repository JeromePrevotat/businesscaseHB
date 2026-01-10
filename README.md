# Business Case Electricity Business

URL: http://electricity-business.91.166.39.12.nip.io/

## How to run

### Dev

- Go to the backend directory: cd ./backend
- Start the Containers: docker compose up
- Go to the frontend directory cd ../frontend
- Install dependencies: npm i
- Start the frontend server: npm start

# Subtree

## Update subtree from original repo

git fetch frontend-repo
git subtree pull --prefix=frontend frontend-repo main
git fetch backend-repo
git subtree pull --prefix=backend backend-repo main

## Push from subtree to original repo

git subtree push --prefix=frontend frontend-repo main
git subtree push --prefix=backend backend-repo main
dum
