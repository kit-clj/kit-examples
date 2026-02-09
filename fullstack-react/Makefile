clean:
	rm -rf target

run:
	clj -M:dev

repl:
	clj -M:dev -M:repl

test:
	clj -M:test

uberjar:
	clj -Sforce -T:build all

client-install:
	cd client && npm install

client-dev:
	cd client && npm run dev

client-build:
	cd client && npm run build

client-deploy: client-build
	rm -rf resources/public/assets
	cp -r client/dist/* resources/public/
