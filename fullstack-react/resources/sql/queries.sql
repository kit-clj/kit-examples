-- :name get-messages :? :*
-- :doc Retrieves all messages ordered by creation time
SELECT id, author, body, created_at
FROM messages
ORDER BY created_at DESC;

-- :name create-message! :! :n
-- :doc Creates a new message
INSERT INTO messages (author, body)
VALUES (:author, :body);

-- :name get-message-by-id :? :1
-- :doc Retrieves a message by its id
SELECT id, author, body, created_at
FROM messages
WHERE id = :id;
