SELECT DISTINCT name FROM people p
JOIN stars ON (p.id = person_id and m.id = movie_id)
JOIN movies m ON (m.id = movie_id)
WHERE year = 2004
ORDER BY birth;
