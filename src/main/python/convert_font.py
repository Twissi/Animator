import re

with open('Font_5x7_extended.h', 'r') as f:
    
    result = {}
    for line in f:
        code = re.search('(?<=code\s)\d+', line)
        
        if not code:
            continue
        
        code = int( code.group())
        result[code] = []
        for matched in re.findall(r'0x[a-fA-F0-9]{2}', line):
            result[code].append(matched)

s = ""
for code in sorted(result):
    itemz = result[code] 
    s += "{{ {0}, {1}, {2}, {3}, {4} }},\n".format(itemz[0], itemz[1], itemz[2], itemz[3], itemz[4], code)

    
print(s)
    