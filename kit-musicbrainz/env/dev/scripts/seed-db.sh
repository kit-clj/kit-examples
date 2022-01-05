#!/usr/bin/env bash

# If you want to load the entire data run this and comment out line 11

#for t in ${PWD}/resources/seed/mbdump/*
#do
#  echo `date` "${t##*/}"
#  psql -d $1 "" -c "\copy ${t##*/} from $t"
#done

psql -d $1 ""  -c "\copy artist from ${PWD}/resources/seed/mbdump/artist"

echo `date`