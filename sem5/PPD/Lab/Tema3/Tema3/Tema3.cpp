#include <iostream>
#include <fstream>
#include <mpi.h>
#include <chrono>

using namespace std;

// mpiexec -n 10 Tema3.exe

string file1 = "D:\\University\\Sem5\\PPD\\Lab\\Tema3\\Tema3\\number1.txt";
string file2 = "D:\\University\\Sem5\\PPD\\Lab\\Tema3\\Tema3\\number2.txt";
string file3 = "D:\\University\\Sem5\\PPD\\Lab\\Tema3\\Tema3\\number3.txt";

void generateNumber(int digitsCount, string filename) {
    ofstream fout(filename);
    if (fout.is_open()) {
        fout << digitsCount << "\n";
        for (int i = 0; i < digitsCount; i++) {
            int digit = rand() % 10;
            fout << digit;
        }
        fout.close();
    }
    else {
        cout << "Unable to open file.";
    }
}

void run1() {
    chrono::steady_clock::time_point startTime, endTime;
    MPI_Status status;
    int p, myRank;

    int first[100000], second[100000], result[100000];

    MPI_Init(NULL, NULL);

    MPI_Comm_size(MPI_COMM_WORLD, &p);
    MPI_Comm_rank(MPI_COMM_WORLD, &myRank);

    if (myRank == 0) {
        startTime = chrono::high_resolution_clock::now();
        ifstream fin1(file1);
        ifstream fin2(file2);

        // decide n value
        int n1, n2, n;
        fin1 >> n1;
        fin2 >> n2;
        if (n1 > n2) n = n1;
        else n = n2;

        // read numbers
        for (int i = 0; i < n; i++) {
            char ch;
            if (i < n1) {
                fin1 >> ch;
                first[n1 - i - 1] = ch - '0';
            }
            if (i < n2) {
                fin2 >> ch;
                second[n2 - i - 1] = ch - '0';
            }
        }
        fin1.close();
        fin2.close();

        // complete the arrays with 0
        if (n1 < n) {
            for (int i = n1; i < n; i++) {
                first[i] = 0;
            }
        }

        if (n2 < n) {
            for (int i = n2; i < n; i++) {
                second[i] = 0;
            }
        }

        // decide how to split the work
        int div = n / (p - 1);
        int mod = n % (p - 1);
        int start = 0, end = div;

        for (int i = 1; i < p; i++) {
            if (mod > 0) {
                mod--;
                end++;
            }
            // send start and end
            MPI_Send(&start, 1, MPI_INT, i, 0, MPI_COMM_WORLD);
            MPI_Send(&end, 1, MPI_INT, i, 0, MPI_COMM_WORLD);

            // send parts of the numbers
            MPI_Send(first + start, end - start, MPI_INT, i, 0, MPI_COMM_WORLD);
            MPI_Send(second + start, end - start, MPI_INT, i, 0, MPI_COMM_WORLD);

            start = end;
            end += div;
        }

        // send carry to first process
        int carry = 0;
        MPI_Send(&carry, 1, MPI_INT, 1, 0, MPI_COMM_WORLD);


        // receive results from processes
        for (int i = 1; i < p; i++) {
            MPI_Recv(&start, 1, MPI_INT, i, 0, MPI_COMM_WORLD, &status);
            MPI_Recv(&end, 1, MPI_INT, i, 0, MPI_COMM_WORLD, &status);
            MPI_Recv(result + start, end - start, MPI_INT, i, 0, MPI_COMM_WORLD, &status);
        }

        // receive carry from the last process
        MPI_Recv(&carry, 1, MPI_INT, p - 1, 0, MPI_COMM_WORLD, &status);

        // write result to file
        ofstream fout(file3);
        if (carry != 0) n++;
        fout << n << "\n";
        if (carry != 0) fout << carry;
        for (int i = n - 1; i >= 0; i--) {
            fout << result[i];
        }
    }
    else {
        int start = 0, end = 0, carry = 0;
        // receive data
        MPI_Recv(&start, 1, MPI_INT, 0, 0, MPI_COMM_WORLD, &status);
        MPI_Recv(&end, 1, MPI_INT, 0, 0, MPI_COMM_WORLD, &status);

        MPI_Recv(first + start, end - start, MPI_INT, 0, 0, MPI_COMM_WORLD, &status);
        MPI_Recv(second + start, end - start, MPI_INT, 0, 0, MPI_COMM_WORLD, &status);

        // receive carry from the previous process
        MPI_Recv(&carry, 1, MPI_INT, myRank - 1, 0, MPI_COMM_WORLD, &status);

        // calculate
        for (int i = start; i < end; i++) {
            int digit = first[i] + second[i] + carry;
            if (digit > 9) {
                carry = 1;
                digit = digit % 10;
            }
            else {
                carry = 0;
            }
            result[i] = digit;
            //cout << "(" << myRank << "): first(" << first[i] << ") second(" << second[i] << ") => digit(" << digit << "), carry(" << carry << ")" << endl;
        }

        // send data to the master and carry to the next process
        MPI_Send(&start, 1, MPI_INT, 0, 0, MPI_COMM_WORLD);
        MPI_Send(&end, 1, MPI_INT, 0, 0, MPI_COMM_WORLD);
        MPI_Send(result + start, end - start, MPI_INT, 0, 0, MPI_COMM_WORLD);

        int dest = myRank + 1;
        if (dest == p) dest = 0;

        MPI_Send(&carry, 1, MPI_INT, dest, 0, MPI_COMM_WORLD);
    }

    MPI_Finalize();
    if (myRank == 0) {
        endTime = chrono::high_resolution_clock::now();
        cout << chrono::duration<double, milli>(endTime - startTime).count();
    }
}

void run2() {
    chrono::steady_clock::time_point startTime, endTime;
    MPI_Status status;
    int p, myRank;

    int first[100000], second[100000], result[100000];

    MPI_Init(NULL, NULL);

    MPI_Comm_size(MPI_COMM_WORLD, &p);
    MPI_Comm_rank(MPI_COMM_WORLD, &myRank);

    ifstream fin1(file1);
    ifstream fin2(file2);

    // decide n value
    int n1, n2, n;
    fin1 >> n1;
    fin2 >> n2;
    if (n1 > n2) n = n1;
    else n = n2;

    if (myRank == 0) {
        startTime = chrono::high_resolution_clock::now();
        // read numbers
        for (int i = 0; i < n; i++) {
            char ch;
            if (i < n1) {
                fin1 >> ch;
                first[n1 - i - 1] = ch - '0';
            }
            if (i < n2) {
                fin2 >> ch;
                second[n2 - i - 1] = ch - '0';
            }
        }
        fin1.close();
        fin2.close();

        // complete the arrays with 0
        if (n1 < n) {
            for (int i = n1; i < n; i++) {
                first[i] = 0;
            }
        }

        if (n2 < n) {
            for (int i = n2; i < n; i++) {
                second[i] = 0;
            }
        }
    }

    int* starts = new int[p];
    int* offsets = new int[p];

    int div = n / p;
    int mod = n % p;
    int start = 0; int end = div;
    int maxim_offset = end - start;

    if (mod > 0) {
        maxim_offset++;
    }

    for (int i = 0; i < p; i++) {

        starts[i] = start;
        offsets[i] = end - start;

        start = end;
        end += div;
    }

    int* aux_first = new int[maxim_offset];
    int* aux_second = new int[maxim_offset];
    int* aux_result = new int[maxim_offset];

    MPI_Scatterv(first, offsets, starts, MPI_INT, aux_first, maxim_offset, MPI_INT, 0, MPI_COMM_WORLD);
    MPI_Scatterv(second, offsets, starts, MPI_INT, aux_second, maxim_offset, MPI_INT, 0, MPI_COMM_WORLD);

    int carry = 0;
    if (myRank > 0) {
        MPI_Recv(&carry, 1, MPI_INT, myRank - 1, 0, MPI_COMM_WORLD, &status);
    }
    
    for (int i = 0; i < offsets[myRank]; i++) {
        int digit = aux_first[i] + aux_second[i] + carry;
        if (digit > 9) {
            carry = 1;
            digit = digit % 10;
        }
        else {
            carry = 0;
        }
        aux_result[i] = digit;
        //cout << "(" << myRank << "): first(" << aux_first[i] << ") second(" << aux_second[i] << ") => digit(" << digit << "), carry(" << carry << ")" << endl;
    }

    if (myRank < p - 1) {
        MPI_Send(&carry, 1, MPI_INT, myRank + 1, 0, MPI_COMM_WORLD);
    }
    else {
        MPI_Send(&carry, 1, MPI_INT, 0, 0, MPI_COMM_WORLD);
    }

    MPI_Gatherv(aux_result, offsets[myRank], MPI_INT, result, offsets, starts, MPI_INT, 0, MPI_COMM_WORLD);

    if (myRank == 0) {
        MPI_Recv(&carry, 1, MPI_INT, p - 1, 0, MPI_COMM_WORLD, &status);

        // write result to file
        ofstream fout(file3);
        if (carry != 0) n++;
        fout << n << "\n";
        if (carry != 0) fout << carry;
        for (int i = n - 1; i >= 0; i--) {
            fout << result[i];
        }
    }

    MPI_Finalize();

    if (myRank == 0) {
        endTime = chrono::high_resolution_clock::now();
        cout << chrono::duration<double, milli>(endTime - startTime).count();
    }
}

int main()
{
    //generateNumber(100, file1);
    //generateNumber(100000, file2);

    //run1();
    run2();
    return 0;
}