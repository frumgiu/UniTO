import hashlib
from random import randint
from random import seed
from nltk.corpus import framenet as fn


def get_frams_IDs():
    return [f.ID for f in fn.frames()]


def getFrameSetForStudent(surname, list_len=5):
    list_frame_id = []
    nof_frames = len(fn.frames())
    base_idx = (abs(int(hashlib.sha512(surname.encode('utf-8')).hexdigest(), 16)) % nof_frames)
    print('\nstudent: ' + surname)
    framenet_IDs = get_frams_IDs()
    i = 0
    offset = 0
    seed(1)
    while i < list_len:
        fID = framenet_IDs[(base_idx + offset) % nof_frames]
        f = fn.frame(fID)
        fNAME = f.name
        print('\tID: {a:4d}\tframe: {framename}'.format(a=fID, framename=fNAME))
        offset = randint(0, nof_frames)
        i += 1
        list_frame_id.append(f)
    return list_frame_id


def print_lus(list_id):
    for f in list_id:
        print('\n____ LUs frame', f.name, ' ____')
        LUs = f.lexUnit.keys()
        for lu in LUs:
            print(lu)


def print_fe(list_id):
    for f in list_id:
        f.FE.keys()
        print('\n____ FEs frame', f.name, ' ____')
        FEs = f.FE.keys()
        for fe in FEs:
            print('\tFE: {}'.format(fe))
