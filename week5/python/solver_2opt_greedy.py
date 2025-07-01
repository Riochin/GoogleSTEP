#!/usr/bin/env python3

import sys
import math

from common import print_tour, read_input

def reconnect(tour, x, y):
    # tourのコピーを作成して変更
    new_tour = tour[:]
    # x+1からy-1の区間を反転
    new_tour[x+1:y] = reversed(new_tour[x+1:y])
    return new_tour

def distance(city1, city2):
    return math.sqrt((city1[0] - city2[0]) ** 2 + (city1[1] - city2[1]) ** 2)


def solve(cities):
    N = len(cities)

    dist = [[0] * N for i in range(N)]
    for i in range(N):
        for j in range(i, N):
            dist[i][j] = dist[j][i] = distance(cities[i], cities[j])

    current_city = 0
    unvisited_cities = set(range(1, N))
    tour = [current_city]

    while unvisited_cities:
        next_city = min(unvisited_cities,
                        key=lambda city: dist[current_city][city])
        unvisited_cities.remove(next_city)
        tour.append(next_city)
        current_city = next_city

    # 2-opt改善を繰り返し適用
    improved = True
    while improved:
        improved = False
        for i in range(N-1):
            for j in range(i+2, N):
                new_tour = reconnect(tour, i, j)
                # TSPの総距離計算（閉路含む）
                old_dist = sum(dist[tour[k]][tour[(k+1)%N]] for k in range(N))
                new_dist = sum(dist[new_tour[k]][new_tour[(k+1)%N]] for k in range(N))
                if new_dist < old_dist:
                    tour = new_tour
                    improved = True
                    break
            if improved:
                break

    return tour


if __name__ == '__main__':
    assert len(sys.argv) > 1
    tour = solve(read_input(sys.argv[1]))
    print_tour(tour)
