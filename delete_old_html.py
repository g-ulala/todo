import os

file_path = "C:/Users/gradr/IdeaProjects/todo/src/main/resources/static/index.html"
if os.path.exists(file_path):
    os.remove(file_path)
    print(f"Deleted {file_path}")
else:
    print(f"{file_path} does not exist")