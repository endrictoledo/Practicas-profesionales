import os
import re

def process_file(filepath):
    with open(filepath, 'r', encoding='utf-8') as f:
        lines = f.readlines()

    new_lines = []
    i = 0
    while i < len(lines):
        line = lines[i]
        
        # Remove empty line comments
        if re.match(r'^\s*//.*', line) and not 'TODO' in line:
            i += 1
            continue
            
        new_lines.append(line)
        i += 1

    # Now add JavaDoc if missing
    final_lines = []
    in_javadoc = False
    last_was_javadoc = False
    
    for i, line in enumerate(new_lines):
        if '/**' in line:
            in_javadoc = True
        if '*/' in line and in_javadoc:
            in_javadoc = False
            last_was_javadoc = True
            final_lines.append(line)
            continue
            
        # If it's an annotation, skip checking for javadoc directly above class for a moment
        if not in_javadoc and not line.strip().startswith('@') and line.strip() != '':
            # Is it a class declaration?
            if re.match(r'^.*(?:public|protected|private)?\s*(?:class|interface)\s+\w+.*$', line):
                # If the preceding lines didn't have a javadoc
                # We need to check if we recently saw a javadoc. A simple way:
                # We check the final_lines backwards. If we hit imports or package before a '*/', it means no javadoc.
                has_javadoc = False
                for prev_line in reversed(final_lines):
                    if prev_line.strip() == '':
                        continue
                    if prev_line.strip().startswith('@'):
                        continue
                    if '*/' in prev_line:
                        has_javadoc = True
                        break
                    if prev_line.strip().startswith('import ') or prev_line.strip().startswith('package '):
                        break
                    
                if not has_javadoc:
                    final_lines.append('/**\n')
                    final_lines.append(' *\n')
                    final_lines.append(' * @author endri\n')
                    final_lines.append(' */\n')
            
        final_lines.append(line)

    # Write back
    with open(filepath, 'w', encoding='utf-8') as f:
        f.writelines(final_lines)

# Walk directory
for root, dirs, files in os.walk('src/main/java'):
    for file in files:
        if file.endswith('.java'):
            process_file(os.path.join(root, file))

print("Done")
