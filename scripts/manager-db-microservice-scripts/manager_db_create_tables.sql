CREATE TABLE "carts" (
	"cart_id"	INTEGER NOT NULL,
	"name"	TEXT NOT NULL,
	"creator_id"	INTEGER NOT NULL,
	"description" TEXT,
	CONSTRAINT "carts_uk" UNIQUE (name, creator_id),
	CONSTRAINT "carts_cart_id_pk" PRIMARY KEY(cart_id)
);

CREATE TABLE "cart_maintainers" (
	"cart_maintainer_id"	INTEGER NOT NULL,
	"cart_id"	INTEGER NOT NULL,
	"maintainer_id"	TEXT NOT NULL,
	"role" TEXT DEFAULT "editor",
	CONSTRAINT "cart_maintainers_uk" UNIQUE (cart_id, maintainer_id),
	CONSTRAINT "cart_maintainers_cart_maintainer_id_pk" PRIMARY KEY(cart_maintainer_id),
	CONSTRAINT "cart_maintainers_cart_id_fk" FOREIGN KEY(cart_id) REFERENCES "carts"("cart_id")
);

CREATE TABLE "cart_items" (
	"cart_item_id"	INTEGER NOT NULL,
	"cart_id"	INTEGER NOT NULL,
	"candidate_id"	INTEGER NOT NULL,
	"score"	FLOAT,
	"notes" TEXT,
	CONSTRAINT "connections_uk" UNIQUE (cart_id, candidate_id),
	CONSTRAINT "cart_items_cart_item_id_pk" PRIMARY KEY(cart_item_id),
	CONSTRAINT "cart_maintainers_cart_id_fk" FOREIGN KEY(cart_id) REFERENCES "carts"("cart_id")
);

CREATE TABLE "connections" (
	"connection_id"	INTEGER NOT NULL,
	"uid_1" INTEGER NOT NULL,
	"uid_2" INTEGER NOT NULL,
	CONSTRAINT "connections_uk" UNIQUE (uid_1, uid_2),
	CONSTRAINT "connections_connection_id_pk" PRIMARY KEY(connection_id)
);

CREATE TABLE "connection_requests" (
	"connection_request_id"	INTEGER NOT NULL,
	"from" INTEGER NOT NULL,
	"to" INTEGER NOT NULL,
	CONSTRAINT "connection_requests_uk" UNIQUE ("from", "to"),
	CONSTRAINT "connection_requests_connection_request_id_pk" PRIMARY KEY(connection_request_id)
);