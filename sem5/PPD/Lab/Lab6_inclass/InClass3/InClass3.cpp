#include <iostream>
#include <mpi.h>
using namespace std;

// mpiexec -n 10 InClass3.exe
void print_vector(int x[], int n)
{
    for (int i = 0; i < n; i++)
        cout << x[i] << " ";
    cout << "\n";
}

//int main()
//{
//    MPI_Status status;
//    int p, myRank;
//    const int n = 10;
//    int a[n], b[n], c[n];
//    int bound = 10;
//    srand(time(NULL));
//
//    MPI_Init(NULL, NULL);
//    MPI_Comm_size(MPI_COMM_WORLD, &p);
//    // salveaza id-ul procesului care ruleaza
//    MPI_Comm_rank(MPI_COMM_WORLD, &myRank);
//
//    if (myRank == 0) {
//        // in master
//        for (int i = 0; i < n; i++)
//        {
//            a[i] = rand() % bound;
//            b[i] = rand() % bound;
//        }
//
//        int intreg = n / (p - 1);
//        int rest = n % (p - 1);
//        int start = 0, end = intreg;
//
//        for (int i = 1; i < p; i++) {
//            if (rest > 0) {
//                rest--;
//                end++;
//            }
//
//            // de unde, cat, ce, cui, tag, comm
//            MPI_Send(&start, 1, MPI_INT, i, 0, MPI_COMM_WORLD);
//            MPI_Send(&end, 1, MPI_INT, i, 0, MPI_COMM_WORLD);
//
//            MPI_Send(a + start, end - start, MPI_INT, i, 0, MPI_COMM_WORLD);
//            MPI_Send(b + start, end - start, MPI_INT, i, 0, MPI_COMM_WORLD);
//
//            start = end;
//            end += intreg;
//        }
//
//        for (int i = 1; i < p; i++) {
//            MPI_Recv(&start, 1, MPI_INT, i, 0, MPI_COMM_WORLD, &status);
//            MPI_Recv(&end, 1, MPI_INT, i, 0, MPI_COMM_WORLD, &status);
//
//            MPI_Recv(c + start, end - start, MPI_INT, i, 0, MPI_COMM_WORLD, &status);
//        }
//
//        print_vector(c, n);
//    }
//    else {
//        int start = 0, end = 0;
//        MPI_Recv(&start, 1, MPI_INT, 0, 0, MPI_COMM_WORLD, &status);
//        MPI_Recv(&end, 1, MPI_INT, 0, 0, MPI_COMM_WORLD, &status);
//
//        MPI_Recv(a + start, end - start, MPI_INT, 0, 0, MPI_COMM_WORLD, &status);
//        MPI_Recv(b + start, end - start, MPI_INT, 0, 0, MPI_COMM_WORLD, &status);
//
//        for (int i = start; i < end; i++) {
//            c[i] = a[i] + b[i];
//        }
//
//        MPI_Send(&start, 1, MPI_INT, 0, 0, MPI_COMM_WORLD);
//        MPI_Send(&end, 1, MPI_INT, 0, 0, MPI_COMM_WORLD);
//        MPI_Send(c + start, end - start, MPI_INT, 0, 0, MPI_COMM_WORLD);
//
////        cout << myRank << " - " << start << " " << end << endl;
////        cout << myRank << "  a: ";
////        print_vector(a, n);
////        cout << myRank << "  b: ";
////        print_vector(b, n);
//    }
//
//    //print_vector(a, n);
//    //print_vector(b, n);
//
//    //cout << p << " " << myRank << endl;
//
//    // incheie procesul; un fel de join
//    MPI_Finalize();
//}

int main() {

    MPI_Status status;
    int p, myRank;
    const int n = 10;
    int bound = 10;
    srand(time(NULL));
    int a[n], b[n], c[n];

    MPI_Init(NULL, NULL);
    MPI_Comm_size(MPI_COMM_WORLD, &p);
    MPI_Comm_rank(MPI_COMM_WORLD, &myRank);


    int* starts = new int[p];
    int* offsets = new int[p];

    int intreg = n / p;
    int rest = n % p;
    int start = 0; int end = intreg;
    int maxim_offset = end - start;

    if (rest > 0) {
        maxim_offset++;
    }

    for (int i = 0; i < p; i++) {

        starts[i] = start;
        offsets[i] = end - start;

        start = end;
        end += intreg;
    }

    int* aux_a = new int[maxim_offset];
    int* aux_b = new int[maxim_offset];
    int* aux_c = new int[maxim_offset];


    if (myRank == 0) {
        for (int i = 0; i < n; i++) {
            a[i] = rand() % bound;
            b[i] = rand() % bound;
        }
    }

    MPI_Scatterv(a, offsets, starts, MPI_INT, aux_a, maxim_offset, MPI_INT, 0, MPI_COMM_WORLD);
    MPI_Scatterv(b, offsets, starts, MPI_INT, aux_b, maxim_offset, MPI_INT, 0, MPI_COMM_WORLD);

    for (int i = 0; i < offsets[myRank]; i++) {
        aux_c[i] = aux_a[i] + aux_b[i];
    }

    MPI_Gatherv(aux_c, offsets[myRank], MPI_INT, c, offsets, starts, MPI_INT, 0, MPI_COMM_WORLD);

    if (myRank == 0) {
        print_vector(c, n);
    }

    MPI_Finalize();
}