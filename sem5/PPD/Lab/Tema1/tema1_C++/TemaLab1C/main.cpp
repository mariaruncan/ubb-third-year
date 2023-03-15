#include <iostream>
#include <string>
#include <thread>
#include <chrono>
#include <fstream>
using namespace std;

int N = 0, M = 0, n, p;
int mat[10000][10], filter[5][5], result[10000][10];
int **mat2, **filter2, **result2;
ifstream finImage("D:\\University\\Semestrul 5\\PPD\\Lab\\TemaLab1\\data.txt");
ifstream finFilter("D:\\University\\Semestrul 5\\PPD\\Lab\\TemaLab1\\filter.txt");

/*
	Thread function
*/
void worker(int iStart, int jStart, int step) {
	int offset = n / 2;
	int i = iStart;
	int j = jStart;

	while (i < N) {
		int newValue = 0;
		for (int k = -offset; k <= offset; k++) {
			for (int l = -offset; l <= offset; l++) {
				int iMat = i - k;
				if (iMat < 0) iMat = 0;
				if (iMat >= N) iMat = N - 1;

				int jMat = j - l;
				if (jMat < 0) jMat = 0;
				if (jMat >= M) jMat = M - 1;

				newValue += mat[iMat][jMat] * filter[k + offset][l + offset];
			}
		}
		result[i][j] = newValue;

		// mergem mai departe
		j = j + step;
		while (j >= M) {
			j = j - M;
			i++;
		}
	}
}

/*
	Applies firter on an image using one thread; image, result and filter are allocated static
*/
void runSequentiallyStatic() {
	// read image
	finImage >> N >> M;
	for (int i = 0; i < N; i++) {
		for (int j = 0; j < M; j++) {
			finImage >> mat[i][j];
		}
	}

	// read filter
	finFilter >> n >> n;
	for (int i = 0; i < n; i++) {
		for (int j = 0; j < n; j++) {
			finFilter >> filter[i][j];
		}
	}

	int result[10][10];
	int offset = n / 2;
	for (int i = 0; i < N; i++) {
		for (int j = 0; j < M; j++) {
			int newValue = 0;
			offset = n / 2;
			for (int k = -offset; k <= offset; k++) {
				for (int l = -offset; l <= offset; l++) {
					int iMat = i - k;
					if (iMat < 0) iMat = 0;
					if (iMat >= N) iMat = N - 1;

					int jMat = j - l;
					if (jMat < 0) jMat = 0;
					if (jMat >= M) jMat = M - 1;

					newValue += mat[iMat][jMat] * filter[k + offset][l + offset];
				}
			}
			result[i][j] = newValue;
		}
	}
}

/*
	Applies firter on an image using p threads; image, result and filter are allocated static
*/
void runParallelStatic() {
	thread threads[16];

	// read image
	finImage >> N >> M;
	for (int i = 0; i < N; i++) {
		for (int j = 0; j < M; j++) {
			finImage >> mat[i][j];
		}
	}

	// read filter
	finFilter >> n >> n;
	for (int i = 0; i < n; i++) {
		for (int j = 0; j < n; j++) {
			finFilter >> filter[i][j];
		}
	}

	int iStart = 0, jStart = 0;
	for (int i = 0; i < p; i++) {
		threads[i] = thread(worker, iStart, jStart, p);
		
		jStart++;
		if (jStart >= M) {
			iStart++;
			jStart -= M;
		}
	}

	for (int i = 0; i < p; i++) {
		threads[i].join();
	}
}

/*
	Applies firter on an image using sequentially; image, result and filter are allocated dynamically
*/
void runSequentiallyDynamic() {
	// read image
	finImage >> N >> M;
	mat2 = new (nothrow) int*[N];
	result2 = new (nothrow) int*[N];
	if (mat2 == nullptr || result2 == nullptr) return;
	for (int i = 0; i < N; i++) {
		mat2[i] = new (nothrow) int[M];
		result2[i] = new (nothrow) int[M];
		if (mat2[i] == nullptr || result2[i] == nullptr) return;
		for (int j = 0; j < M; j++) {
			finImage >> mat2[i][j];
		}
	}

	// read filter
	finFilter >> n >> n;
	filter2 = new (nothrow) int* [n];
	if (filter2 == nullptr) return;
	for (int i = 0; i < n; i++) {
		filter2[i] = new (nothrow) int[n];
		if (filter2[i] == nullptr) return;
		for (int j = 0; j < n; j++) {
			finFilter >> filter[i][j];
		}
	}

	int offset = n / 2;
	for (int i = 0; i < N; i++) {
		for (int j = 0; j < M; j++) {
			int newValue = 0;
			for (int k = -offset; k <= offset; k++) {
				for (int l = -offset; l <= offset; l++) {
					int iMat = i - k;
					if (iMat < 0) iMat = 0;
					if (iMat >= N) iMat = N - 1;

					int jMat = j - l;
					if (jMat < 0) jMat = 0;
					if (jMat >= M) jMat = M - 1;

					newValue += mat2[iMat][jMat] * filter2[k + offset][l + offset];
				}
			}
			result2[i][j] = newValue;
		}
	}
}

/*
	Applies firter on an image using p threads; image, result and filter are allocated dynamically
*/
void runParallelDynamic() {
	thread threads[16];

	// read image
	finImage >> N >> M;
	mat2 = new (nothrow) int* [N];
	result2 = new (nothrow) int* [N];
	if (mat2 == nullptr || result2 == nullptr) return;
	for (int i = 0; i < N; i++) {
		mat2[i] = new (nothrow) int[M];
		result2[i] = new (nothrow) int[M];
		if (mat2[i] == nullptr || result2[i] == nullptr) return;
		for (int j = 0; j < M; j++) {
			finImage >> mat2[i][j];
		}
	}

	// read filter
	finFilter >> n >> n;
	filter2 = new (nothrow) int* [n];
	if (filter2 == nullptr) return;
	for (int i = 0; i < n; i++) {
		filter2[i] = new (nothrow) int[n];
		if (filter2[i] == nullptr) return;
		for (int j = 0; j < n; j++) {
			finFilter >> filter[i][j];
		}
	}

	int iStart = 0, jStart = 0;
	for (int i = 0; i < p; i++) {
		threads[i] = thread(worker, iStart, jStart, p);

		jStart++;
		if (jStart >= M) {
			iStart++;
			jStart -= M;
		}
	}

	for (int i = 0; i < p; i++) {
		threads[i].join();
	}
}

int main(int argc, char** argv) {
	auto startTime = chrono::high_resolution_clock::now();

	p = stoi(argv[1]);
	if (p == 1) {
		//runSequentiallyStatic();
		runSequentiallyDynamic();
	}
	else {
		//runParallelStatic();
		runParallelDynamic();
	}

	auto endTime = chrono::high_resolution_clock::now();
	cout << chrono::duration<double, milli>(endTime - startTime).count();
	return 0;
}