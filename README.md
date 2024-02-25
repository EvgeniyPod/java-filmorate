# java-filmorate
![Untitled](https://github.com/EvgeniyPod/java-filmorate/assets/137653952/49accdba-69a9-4609-82b4-68f9cb24a83d)

---
___Запросы:___ 

1. Вывести все фильмы вместе с жанрами:
```
SELECT f.film_id, f.name, g.name
FROM films AS f
JOIN film_genre AS fg ON f.film_id = fg.film_id
JOIN genre AS g ON fg.genre_id = g.genre_id;
```
2. Получить количество лайков для каждого фильма:
```
SELECT f.film_id, f.name, COUNT(ul.user_id) AS like_count
FROM films AS f
LEFT JOIN user_likes AS ul ON f.film_id = ul.film_id
GROUP BY f.film_id, f.name;
```
3. Найти все фильмы, которые понравились конкретному пользователю:
```
SELECT f.film_id, f.name
FROM films AS f
JOIN user_likes AS ul ON f.film_id = ul.film_id
WHERE ul.user_id = <user_id>;
```
4. Найти всех друзей конкретного пользователя:
```
SELECT u.name AS friend_name
FROM users AS u
JOIN user_friends AS uf ON u.user_id = uf.friend_id
WHERE uf.user_id = <user_id> AND uf.friendship = true;
```
5. Получить список категорий фильмов вместе с количеством фильмов в каждой категории:
```
SELECT c.category_name, COUNT(f.film_id) AS film_count
FROM category AS c
LEFT JOIN films AS f ON c.category_id = f.category_id
GROUP BY c.category_name;
```
