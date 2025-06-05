import random, sys, time

# Hash function.
#
# |key|: string
# Return value: a hash value
def calculate_hash(key):
    assert type(key) == str
    # Note: This is not a good hash function. Do you see why?
    hash = 1
    for i in key:
        # 💡 += を *=にしたら、衝突が減ったのかびっくりするくらい早くなった！！！！！
        hash = hash * ord(i) % 1000000009
    return hash


# An item object that represents one key - value pair in the hash table.
class Item:
    def __init__(self, key, value, next):
        assert type(key) == str
        self.key = key
        self.value = value
        self.next = next

class HashTable:

    # Initialize the hash table.
    def __init__(self):
        # Set the initial bucket size to 97. A prime number is chosen to reduce
        # hash conflicts.
        self.bucket_size = 97
        self.buckets = [None] * self.bucket_size
        self.item_count = 0

    # 💡 再ハッシュの実装
    def rehash(self,new_bucket_size):
        old_buckets = self.buckets
        self.buckets = [None] * new_bucket_size #
        old_bucket_size = self.bucket_size
        self.bucket_size = new_bucket_size
        self.item_count = 0

        for head in old_buckets:
            item = head
            while item:
                self.put(item.key, item.value)
                item = item.next
    
    def put(self, key, value):
        assert type(key) == str
        self.check_size() # Note: Don't remove this code.
        bucket_index = calculate_hash(key) % self.bucket_size
        item = self.buckets[bucket_index]
        while item:
            if item.key == key:
                item.value = value
                return False
            item = item.next
        new_item = Item(key, value, self.buckets[bucket_index])
        self.buckets[bucket_index] = new_item
        self.item_count += 1

        # 💡 再ハッシュの確認
        if self.item_count >= self.bucket_size * 0.7:
            self.rehash(self.bucket_size * 2)

        return True

    def get(self, key):
        assert type(key) == str
        #  ❓ これ何👇
        self.check_size() # Note: Don't remove this code. <- ❓ これ何
        bucket_index = calculate_hash(key) % self.bucket_size
        item = self.buckets[bucket_index]
        while item:
            if item.key == key:
                return (item.value, True)
            item = item.next
        return (None, False)


    def delete(self, key):
        assert type(key) == str

        self.check_size()
        bucket_index = calculate_hash(key) % self.bucket_size
        item = self.buckets[bucket_index]
        prev = None

        while item:
            if item.key == key:
                if prev is None:
                    self.buckets[bucket_index] = item.next
                else:
                    prev.next = item.next
                self.item_count -= 1

                # 💡 再ハッシュの確認
                if self.item_count <= self.bucket_size * 0.3:
                    self.rehash(self.bucket_size // 2)
                return True
            prev = item
            item = item.next
        return (False)
    
    # 💡 追加で実装、ほぼgetと同じ！
    def contains_key(self,key):
        assert type(key) == str
        bucket_index = calculate_hash(key) % self.bucket_size
        item = self.buckets[bucket_index]
        while item:
            if item.key == key:
                return True
            item = item.next
        return False
    
    # 💡 こっちも追加！バケットごとにみてvalueをappendした
    def get_value_array(self):
        values = []
        for b in self.buckets:
            item = b
            while item:
                values.append(item.value)
                item = item.next

        return values
    
    # 💡 こっちも追加！
    def get_key_array(self):
        keys = []
        for b in self.buckets:
            item = b
            while item:
                keys.append(item.key)
                item = item.next

        return keys

    # Return the total number of items in the hash table.
    def size(self):
        return self.item_count

    def check_size(self):
        pass
   