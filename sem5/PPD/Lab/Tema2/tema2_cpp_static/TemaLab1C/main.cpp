#include <iostream>
#include <string>
#include <thread>
#include <chrono>
#include <condition_variable>
#include <fstream>
using namespace std;

int N = 0, M = 0, n = 0, p;
int mat[10000][10], filter[5][5];
ifstream finImage("D:\\University\\Sem5\\PPD\\Lab\\Tema2\\tema2_java\\data.txt");
ifstream finFilter("D:\\University\\Sem5\\PPD\\Lab\\Tema2\\tema2_java\\filter.txt");
ifstream finSolution("D:\\University\\Sem5\\PPD\\Lab\\Tema2\\tema2_java\\seq_out.txt");

class Barrier {
public:
	explicit Barrier(std::size_t iCount) :
		mThreshold(iCount),
		mCount(iCount),
		mGeneration(0) {
	}

	void Wait() {
		unique_lock<mutex> lLock{ mMutex };
		auto lGen = mGeneration;
		if (!--mCount) {
			mGeneration++;
			mCount = mThreshold;
			mCond.notify_all();
		}
		else {
			mCond.wait(lLock, [this, lGen] { return lGen != mGeneration; });
		}
	}

private:
	mutex mMutex;
	condition_variable mCond;
	size_t mThreshold;
	size_t mCount;
	size_t mGeneration;
};

Barrier barrier(16);

void read() {
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
}

void validate() {
	// read solution
	int rows, cols;
	finSolution >> rows >> cols;

	int** solution;
	solution = new (nothrow) int* [rows];
	if (solution == nullptr) return;
	for (int i = 0; i < rows; i++) {
		solution[i] = new (nothrow) int[cols];
		if (solution[i] == nullptr) return;
		for (int j = 0; j < cols; j++) {
			finSolution >> solution[i][j];
		}
	}

	for (int i = 0; i < N; i++) {
		for (int j = 0; j < M; j++) {
			if (solution[i][j] != mat[i][j])
				throw new exception("Invalid solution!");
		}
	}

	for (int i = 0; i < rows; i++)
		delete solution[i];
	delete solution;
}


int** copyNeighbours(int iStart, int jStart, int count) {
	const int tmpRows = n;
	const int tmpCols = n * count;
	int iCopy = iStart, jCopy = jStart;
	int offset = tmpRows / 2;
	int** tmp;

	tmp = new (nothrow) int* [tmpRows];
	if (tmp == nullptr) return nullptr;
	for (int i = 0; i < tmpRows; i++) {
		tmp[i] = new (nothrow) int[tmpCols];
		if (tmp[i] == nullptr) return nullptr;
	}

	for (int k = 0; k < count; k++) { // pt fiecare elem, copiem zona lui
		for (int i = 0; i < n; i++) {
			for (int j = 0; j < n; j++) {
				int iMat = iCopy + i - offset;
				if (iMat < 0) iMat = 0;
				if (iMat >= N) iMat = N - 1;

				int jMat = jCopy + j - offset;
				if (jMat < 0) jMat = 0;
				if (jMat >= M) jMat = M - 1;

				tmp[i][j + n * k] = mat[iMat][jMat];
			}
		}

		jCopy++;
		if (jCopy >= M) {
			iCopy++;
			jCopy -= M;
		}
	}

	return tmp;
}

/*
	Thread function
*/
void worker(int iStart, int jStart, int count) {
	// copy
	int** tmp = copyNeighbours(iStart, jStart, count);

	// wait
	barrier.Wait();

	// work
	int i = iStart, j = jStart;

	for (int zona = 0; zona < count; zona++) {
		int newValue = 0;
		for (int iTmp = 0; iTmp < n; iTmp++) {
			for (int jTmp = 0; jTmp < n; jTmp++) {
				int x = tmp[iTmp][jTmp + n * zona];
				int y = filter[iTmp][jTmp];
				newValue += x * y;
			}
		}

		mat[i][j] = newValue;

		j++;
		if (j >= M) {
			i++;
			j -= M;
		}
	}

	for (int i = 0; i < n; i++)
		delete tmp[i];
	delete tmp;
}

/*
	Applies firter on an image using one thread; image, result and filter are allocated static
*/
void runSequentially() {
	auto startTime = chrono::high_resolution_clock::now();

	read();

	int offset = n / 2;
	int** tmp = new int* [N];
	for (int i = 0; i < N; i++) {
		tmp[i] = new int[M];
		for (int j = 0; j < M; j++) {
			tmp[i][j] = mat[i][j];
		}
	}

	for (int i = 0; i < N; i++) {
		for (int j = 0; j < M; j++) {
			int newValue = 0;
			for (int k = 0; k < n; k++) {
				for (int l = 0; l < n; l++) {
					int iMat = i + k - offset;
					if (iMat < 0) iMat = 0;
					if (iMat >= N) iMat = N - 1;

					int jMat = j + l - offset;
					if (jMat < 0) jMat = 0;
					if (jMat >= M) jMat = M - 1;

					int x = tmp[iMat][jMat];
					int y = filter[k][l];

					newValue += x * y;
				}
			}
			mat[i][j] = newValue;
		}
	}

	for (int i = 0; i < N; i++)
		delete tmp[i];
	delete tmp;

	auto endTime = chrono::high_resolution_clock::now();
	cout << chrono::duration<double, milli>(endTime - startTime).count();
}

/*
	Applies firter on an image using p threads; image, result and filter are allocated static
*/
void runParallel() {
	thread threads[16];

	auto startTime = chrono::high_resolution_clock::now();

	read();

	int iStart = 0, jStart = 0;
	int div = (N * M) / p;
	int mod = (N * M) % p;

	for (int i = 0; i < p; i++) {
		int count = div;
		if (i < mod) count++;

		threads[i] = thread(worker, iStart, jStart, count);
		
		jStart += count;
		while (jStart >= M) {
			iStart++;
			jStart -= M;
		}
	}

	for (int i = 0; i < p; i++) {
		threads[i].join();
	}

	auto endTime = chrono::high_resolution_clock::now();
	cout << chrono::duration<double, milli>(endTime - startTime).count();

	validate();
}

int main(int argc, char** argv) {
	p = stoi(argv[1]);
	if (p == 1) {
		runSequentially();
	}
	else {
		runParallel();
	}

	return 0;
}