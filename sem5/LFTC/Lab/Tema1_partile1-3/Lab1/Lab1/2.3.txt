#include <iostream>
using namespace std;

int main()
{
	int n, number, sum;
	cin >> n;
	sum = 0;
	while (n > 0) {
		cin >> number;
		sum = sum + number;
		n = n - 1;
	}
	cout << sum;
}