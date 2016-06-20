#ifndef HASH_KIT_H
#define HASH_KIT_H

#include <stdint.h>
#include <stdlib.h>
#include <

namespace nc_util
{

/*
 * Functions and types for CRC Check
 */
uint32_t hash_crc16(const char *key, size_t key_length);
uint32_t hash_crc32(const char *key, size_t key_length);
uint32_t hash_crc32a(const char *key, size_t key_length);

/*
 * Functions and types for FNV Hash Check
 */
uint32_t hash_fnv1_64(const char *key, size_t key_length);
uint32_t hash_fnv1a_64(const char *key, size_t key_length);

uint32_t hash_fnv1_32(const char *key, size_t key_length);
uint32_t hash_fnv1a_32(const char *key, size_t key_length);


/*
 * MurMur Hash Check
 */
uint32_t hash_murmur(const char *key, size_t length);



}


#endif
