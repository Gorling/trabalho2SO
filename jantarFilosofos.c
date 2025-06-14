#include <stdio.h>
#include <stdlib.h>
#include <pthread.h>
#include <semaphore.h>
#include <unistd.h>

#define N 5
#define LEFT (i + N - 1) % N
#define RIGHT (i + 1) % N
#define THINKING 0
#define HUNGRY 1
#define EATING 2

int state[N];
sem_t forks[N];
sem_t mutex;
pthread_t philosophers[N];

void test(int i) {
    if (state[i] == HUNGRY &&
        state[LEFT] != EATING &&
        state[RIGHT] != EATING) {
        state[i] = EATING;
        sem_post(&forks[i]);
    }
}

void take_forks(int i) {
    sem_wait(&mutex);
    state[i] = HUNGRY;
    printf("Filósofo %d está com fome\n", i);
    test(i);
    sem_post(&mutex);
    sem_wait(&forks[i]);
}

void put_forks(int i) {
    sem_wait(&mutex);
    state[i] = THINKING;
    printf("Filósofo %d terminou de comer e está pensando\n", i);
    test(LEFT);
    test(RIGHT);
    sem_post(&mutex);
}

void* philosopher(void* arg) {
    int i = *(int*)arg;
    while (1) {
        printf("Filósofo %d está pensando\n", i);
        sleep(rand() % 3);
        take_forks(i);
        printf("Filósofo %d está comendo\n", i);
        sleep(rand() % 3);
        put_forks(i);
    }
    return NULL;
}

int main() {
    srand(time(NULL));

    sem_init(&mutex, 0, 1);
    for (int i = 0; i < N; i++) {
        sem_init(&forks[i], 0, 0);
        state[i] = THINKING;
    }

    int ids[N];
    for (int i = 0; i < N; i++) {
        ids[i] = i;
        pthread_create(&philosophers[i], NULL, philosopher, &ids[i]);
    }

    for (int i = 0; i < N; i++) {
        pthread_join(philosophers[i], NULL);
    }

    sem_destroy(&mutex);
    for (int i = 0; i < N; i++) {
        sem_destroy(&forks[i]);
    }

  //Nomes: Arthur Spironello, Gabriel Machado e Yuri Alexander

    return 0;
}
