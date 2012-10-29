#include "stdafx.h"
#include "Word.h"

template <class T>
void deleteVectorOfPointers( T * inVectorOfPointers )
{
    T::iterator i;
    for ( i = inVectorOfPointers->begin() ; i < inVectorOfPointers->end(); i++ )
    {
        delete * i;
    }
    delete inVectorOfPointers;
}

Word::Word()
{
}


Word::~Word()
{
	deleteVectorOfPointers(pinyins);
	delete characters;
}

