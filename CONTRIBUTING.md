# Contributing to Full-Stack Java Projects

Thank you for your interest in contributing to this portfolio project! This document provides guidelines for contributions.

## 🎯 Project Goals

This repository serves as a **professional portfolio** demonstrating:
- Enterprise-level Java application development
- Modern full-stack architecture
- Best practices in software engineering
- Production-ready code quality

## 📋 Code Standards

### Java Code Style

- **Java Version:** Java 17 (LTS)
- **Formatting:** Follow Google Java Style Guide
- **Naming Conventions:** 
  - Classes: PascalCase
  - Methods/Variables: camelCase
  - Constants: UPPER_SNAKE_CASE
- **Comments:** JavaDoc for all public classes and methods
- **Testing:** Minimum 70% code coverage

### React Code Style

- **ES Version:** ES6+
- **Components:** Functional components with hooks
- **File Structure:** One component per file
- **Naming:** PascalCase for components, camelCase for utilities

## 🔧 Development Process

### 1. Fork & Clone

```bash
# Fork the repository on GitHub
# Clone your fork
git clone https://github.com/YOUR_USERNAME/fullstack-java-projects.git
cd fullstack-java-projects
```

### 2. Create Branch

```bash
git checkout -b feature/your-feature-name
```

Branch naming conventions:
- `feature/` - New features
- `bugfix/` - Bug fixes
- `docs/` - Documentation updates
- `refactor/` - Code refactoring

### 3. Make Changes

- Write clean, documented code
- Follow existing patterns and conventions
- Add tests for new functionality
- Update documentation as needed

### 4. Test Your Changes

```bash
# Backend tests
cd <project>/backend
mvn test

# Frontend tests  
cd <project>/frontend
npm test

# Integration tests
mvn verify
```

### 5. Commit Changes

```bash
git add .
git commit -m "feat: add new feature description"
```

Commit message format:
- `feat:` - New feature
- `fix:` - Bug fix
- `docs:` - Documentation
- `refactor:` - Code refactoring
- `test:` - Adding tests
- `chore:` - Maintenance tasks

### 6. Push & Create PR

```bash
git push origin feature/your-feature-name
```

Then create a Pull Request on GitHub with:
- Clear description of changes
- Why the change is needed
- How it was tested
- Screenshots (if UI changes)

## ✅ Pull Request Checklist

- [ ] Code follows project style guidelines
- [ ] All tests pass (`mvn test`)
- [ ] New code has test coverage
- [ ] JavaDoc added for new public methods
- [ ] Documentation updated (README, API_DOCS)
- [ ] No unnecessary dependencies added
- [ ] Commit messages are clear and descriptive

## 🐛 Reporting Bugs

### Before Submitting

1. Check existing issues
2. Try to reproduce with latest version
3. Gather relevant information

### Bug Report Template

```markdown
**Description:**
Clear description of the bug

**Steps to Reproduce:**
1. Step one
2. Step two
3. ...

**Expected Behavior:**
What should happen

**Actual Behavior:**
What actually happens

**Environment:**
- OS: [Windows/Mac/Linux]
- Java Version: [17]
- Browser: [Chrome 120]
- Project: [e-commerce/food-ordering/event-booking/job-portal]

**Logs/Screenshots:**
[Attach relevant logs or screenshots]
```

## 💡 Feature Requests

Feature requests are welcome! Please provide:
- Clear use case
- Expected benefit
- Proposed implementation (optional)

## 📝 Documentation

When adding features:
- Update relevant README files
- Add JavaDoc comments
- Update API_DOCUMENTATION.md if endpoints change
- Add examples where appropriate

## 🧪 Testing Guidelines

### Writing Tests

```java
@Test
void methodName_scenario_expectedOutcome() {
    // Given: Setup
    // When: Execute
    // Then: Assert
}
```

### Test Coverage

- All service methods should have unit tests
- All controller endpoints should have integration tests
- Edge cases should be covered
- Error scenarios should be tested

## 🤝 Code Review Process

All contributions will be reviewed for:
- Code quality and clarity
- Test coverage
- Documentation completeness
- Security considerations
- Performance implications

## 📧 Contact

For questions or discussions:
- Open an issue on GitHub
- Tag with appropriate labels
- Be respectful and constructive

## 🙏 Thank You

Your contributions help improve this portfolio project and demonstrate collaborative development skills!

---

**Happy Coding!** 🚀
