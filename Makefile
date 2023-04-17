build:
	docker build -t backend-lab6-node .

run:
	docker run -p 3000:3000 backend-lab6-node:latest
