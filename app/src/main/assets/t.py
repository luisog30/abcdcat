#!/usr/bin/env python
# -*- coding: utf-8 -*-

import codecs
import sys

l = list()

with codecs.open(sys.argv[1], "r", encoding = "utf-8") as f:
    l = f.read().splitlines()

ll = list()

for i in l:
    ll.append(i.split("$"))
    print len(ll[-1])

print len(ll)

