SELECT DISTINCT name FROM people
WHERE id in
(SELECT person_id FROM stars where movie_id in
(SELECT movie_id FROM stars where person_id =
(SELECT id FROM people WHERE name = 'Kevin Bacon' and birth = 1958)))
and id !=
(SELECT id FROM people WHERE name = 'Kevin Bacon' and birth = 1958);
