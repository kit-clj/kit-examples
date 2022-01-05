-- place your sql queries here
-- see https://www.hugsql.org/ for documentation


-- :name artist-by-id :? :1
select * from artist where id = :id limit 1;