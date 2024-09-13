SELECT DISTINCT title FROM movies m
where id in
(SELECT movie_id FROM stars where person_id in
(SELECT id FROM people WHERE name = 'Bradley Cooper'))
AND
id in
(SELECT movie_id FROM stars where person_id in
(SELECT id FROM people WHERE name = 'Jennifer Lawrence'));
