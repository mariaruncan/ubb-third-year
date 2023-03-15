#include <iostream>
#include <vector>
#include <thread>
#include <chrono>
using namespace std;

void printVector(vector<int> v) {
	for (int el : v) {
		cout << el << " ";
	}
	cout << "\n";
}

int operatie(int a, int b) {
	return sqrt(pow(a, 4) + pow(b, 4));
}

void runSecvential(vector<int>& a, vector<int>& b, vector<int>& c, int n, int bound) {
	auto startTime = chrono::high_resolution_clock::now();

	for (int i = 0; i < n; i++) {
		c[i] = operatie(a[i], b[i]);
	}

	auto endTime = chrono::high_resolution_clock::now();
	cout << "Secvential time: " << chrono::duration<double, milli>(endTime - startTime).count() << "\n";
	//printVector(c);
}

void threadRun(int start, int end, vector<int>& a, vector<int>& b, vector<int>& c) {
	for (int i = start; i < end; i++) {
		c[i] = operatie(a[i], b[i]);
	}
}

void runParalel(vector<int>& a, vector<int>& b, vector<int>& d, int n, int p) {
	auto startTime = chrono::high_resolution_clock::now();

	int intreg = n / p;
	int rest = n % p;
	int start = 0;
	int end = intreg;

	vector<thread> threads(p);

	for (int i = 0; i < p; i++) {
		if (rest > 0) {
			end++;
			rest--;
		}
		threads[i] = thread(threadRun, start, end, ref(a), ref(b), ref(d));
		start = end;
		end += intreg;
	}

	for (int i = 0; i < p; i++) {
		threads[i].join();
	}

	auto endTime = chrono::high_resolution_clock::now();
	cout << "Paralel time: " << chrono::duration<double, milli>(endTime - startTime).count() << "\n";
	//printVector(d);
}

void initVector(vector<int>& a, vector<int>& b, int n, int bound) {
	for (int i = 0; i < n; i++) {
		a[i] = rand() % bound;
		b[i] = rand() % bound;
	}
}

int main()
{
	int n = 1000000, bound = 900000;
	vector<int> a(n);
	vector<int> b(n);
	vector<int> c(n); // rez secvential
	vector<int> d(n); // rez paralel
	int p = 8; // nr threads
	int intreg, rest;

	initVector(ref(a), ref(b), n, bound);
	runSecvential(ref(a), ref(b), ref(c), n, bound);
	runParalel(ref(a), ref(b), ref(d), n, p);
	return 0;
}
