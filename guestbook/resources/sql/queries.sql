-- :name save-message! :! :n
-- :doc creates a new message
INSERT INTO guestbook
(name, message)
VALUES (:name, :message)

-- :name get-messages :? :*
-- :doc selects all available messages
SELECT * FROM guestbook