SELECT DISTINCT name FROM people p
JOIN directors d ON (p.id = d.person_id AND m.id = d.movie_id)
JOIN movies m ON (m.id = d.movie_id)
JOIN ratings r ON (m.id = r.movie_id)
WHERE rating >= 9.0;
