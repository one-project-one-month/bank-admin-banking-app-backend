### Ticket ID: Banking-FE001 or Banking-BE001 or Banking-MB001 (FE for frontend) (BE for backend) (MB for Mobile)
> ✅ FE for frontend, BE for backend, MB for Mobile

## Summary
Provision S3 Bucket for Static Site Deployment via Terraform

## Description
Trnx preparation page steup 

## Tasks
Setup S3 bucket via Terraform


## Acceptance Criteria
Terraform state must stored in remote environments.
S3 bucket must include necessary permissions and configs for website deployment.

## 🌿 Git Workflow Instructions

### 1. **Sync with `dev` branch**

Before starting work:

```bash
git checkout dev
git pull origin dev
```

### 2. **Create a new feature branch**

```bash
git switch -c ft/corporate-banking-001
```

> ✅ corporate keyword is only for repository that starts with corporate, If repo name starts with "user", branch name should be ft/user-banking-001

### 3. **Make your changes**

Work on your task as described above.

### 4. **Add and commit your changes**

Use `git commit` to create a conventional commit message:

```bash
git add . or git add file1 flle2
git commit -m "your commit-message"
```

> ✅ Write the appropriate commit message with your changes

### 5. **Push your branch**

```bash
git push origin ft/corporate-banking-001
```

### ✅ Once done

Open a Pull Request to `dev` and request a review.
