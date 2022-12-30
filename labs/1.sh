
#!/usr/bin/bash
filename="$1"
while read -r line; do
 name="$line"
 echo "Setting lab $name"
 echo "lab $name start"
 echo "Lab Setup Done"
 echo " copying lab files to git dir"
 cp  -r /home/student/DO378/labs/$name ./labs/$name
 cp  -r /home/student/DO378/solutions/$name ./solutions/$name
 echo "Pushing to git repository"
 git add *
 git commit -m "$name lab setup"
 git push
 echo delete lab
 lab $name finish
done < "$filename"
