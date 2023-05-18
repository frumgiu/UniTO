from nltk.corpus.reader import Synset

partitive_target = {
    'Partitive': Synset('partitive.n.01'),
    'Subset': Synset('subset.n.01'),
    'Group': Synset('group.n.01'),
    'of.prep': None,
    'out of.prep': None
}

speed_target = {
    'Speed_description': Synset('speed.n.02'),
    'Entity': Synset('entity.n.01'),
    'Attribute': Synset('attribute.n.02'),
    'Speed': Synset('speed.n.01'),
    'Degree': Synset('degree.n.01'),
    'Time': Synset('time.n.02'),
    'Circumstances': Synset('circumstances.n.01'),
    'Explanation': Synset('explanation.n.01'),
    'fast.a': Synset('fast.a.01'),
    'quick.a': Synset('quick.a.03'),
    'speedy.a': Synset('speedy.a.02'),
    'speed.n': Synset('speed.n.02'),
    'breakneck.a': Synset('breakneck.a.01'),
    'rate.n': Synset('rate.n.01'),
    'pace.n': Synset('pace.n.03'),
    'rapidly.adv': Synset('rapidly.adv.01'),
    'speedily.adv': Synset('speedily.adv.01'),
    'smart.a': Synset('smart.a.06'),
    'rapid.a': Synset('rapid.a.02')
}
mining_target = {
    'Mining': Synset('mining.n.01'),
    'Miner': Synset('miner.n.01'),
    'Resource': Synset('resource.n.02'),
    'Place': Synset('place.n.01'),
    'Means': Synset('means.n.01'),
    'Outcome': Synset('result.n.03'),
    'Time': Synset('time.n.02'),
    'mine.v': Synset('mine.v.01'),
    'miner.n': Synset('miner.n.01'),
    'extract.v': Synset('extract.v.01')
}

receiving_target = {
    'Receiving': Synset('receive.v.01'),
    'Recipient': Synset('recipient.n.01'),
    'Donor': Synset('donor.n.02'),
    'Theme': Synset('subject.n.01'),
    'Purpose_of_donor': None,
    'Means': Synset('means.n.01'),
    'Time': Synset('time.n.02'),
    'Place': Synset('topographic_point.n.01'),
    'Mode_of_transfer': None,
    'Depictive': Synset('delineative.s.01'),
    'Manner': Synset('manner.n.01'),
    'Countertransfer': None,
    'Path': Synset('way.n.05'),
    'Purpose_of_theme': None,
    'Role': Synset('function.n.03'),
    'receive.v': Synset('receive.v.01'),
    'accept.v': Synset('accept.v.02'),
    'receipt.n': Synset('reception.n.04')
}

bail_target = {
    'Bail_decision': Synset('bail.n.01'),
    'Accused': Synset('accused.n.01'),
    'Judge': Synset('judge.n.01'),
    'Status': Synset('status.n.01'),
    'Time': Synset('time.n.02'),
    'Place': Synset('opographic_point.n.01'),
    'Manner': Synset('manner.n.01'),
    'Means': Synset('means.n.01'),
    'Purpose': Synset('determination.n.02'),
    'set.v': Synset('set.v.04'),
    'fix.v': Synset('fix.v.06'),
    'order.v': Synset('order.v.01'),
    'bail.n': Synset('bail.n.01'),
    'bond.n': Synset('bond.n.02')
}
