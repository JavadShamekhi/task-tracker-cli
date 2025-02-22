# Task Tracker CLI

A simple command-line task tracker built with Java and Spring Boot.

## Features
- Add, update, and delete tasks
- View task lists from the terminal
- Simple and efficient CLI interface

## Installation
1. Clone the repository:
   ```sh
   git clone https://github.com/JavadShamekhi/task-tracker-cli.git
   cd task-tracker-cli

2. Run the project
    mvn spring-boot:run

## Usage
    java -jar task-tracker-cli.jar add "Finish the project"

    task-cli add "<description>"
	task-cli update <id> "<new description>"
	task-cli delete <id>
	task-cli mark-in-progress <id>
    task-cli mark-done <id>
	task-cli list
	task-cli list done
	task-cli list todo
    task-cli list in-progress

## Project URL
    https://github.com/JavadShamekhi/task-tracker-cli
    https://roadmap.sh/projects/task-tracker
