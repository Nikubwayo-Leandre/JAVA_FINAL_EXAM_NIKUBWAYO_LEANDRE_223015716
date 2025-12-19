package service;

import dao.InstructorDAO;
import model.Instructor;
import java.time.LocalDate;
import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;

public class InstructorService {
    private final InstructorDAO instructorDAO;

    public InstructorService() {
        this.instructorDAO = new InstructorDAO();
    }

    // Create a new instructor
    public Instructor createInstructor(String name, String identifier) {
        try {
            Instructor instructor = new Instructor(name, identifier);
            instructor.setStatus("Active"); // Default status
            
            int instructorId = instructorDAO.create(instructor);
            if (instructorId > 0) {
                instructor.setInstructorId(instructorId);
                System.out.println("✅ Instructor created successfully: " + instructor.getName() + " (ID: " + instructorId + ")");
                return instructor;
            } else {
                System.out.println("❌ Failed to create instructor: " + name);
                return null;
            }
        } catch (Exception e) {
            System.err.println("❌ Error creating instructor: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }

    // Create instructor with full details
    public Instructor createInstructorWithDetails(String name, String identifier, String status, 
                                                String location, String contact, LocalDate assignedSince) {
        try {
            Instructor instructor = new Instructor(name, identifier);
            instructor.setStatus(status != null ? status : "Active");
            instructor.setLocation(location);
            instructor.setContact(contact);
            instructor.setAssignedSince(assignedSince);
            
            int instructorId = instructorDAO.create(instructor);
            if (instructorId > 0) {
                instructor.setInstructorId(instructorId);
                System.out.println("✅ Instructor created successfully: " + instructor.getName());
                return instructor;
            } else {
                System.out.println("❌ Failed to create instructor: " + name);
                return null;
            }
        } catch (Exception e) {
            System.err.println("❌ Error creating instructor with details: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }

    // Get instructor by ID
    public Instructor getById(int instructorId) {
        try {
            Instructor instructor = instructorDAO.findById(instructorId);
            if (instructor != null) {
                System.out.println("✅ Retrieved instructor: " + instructor.getName());
            } else {
                System.out.println("❌ Instructor not found with ID: " + instructorId);
            }
            return instructor;
        } catch (Exception e) {
            System.err.println("❌ Error retrieving instructor: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }

    // Get all instructors - THIS WAS MISSING!
    public List<Instructor> getAll() {
        try {
            List<Instructor> instructors = instructorDAO.findAll();
            System.out.println("✅ Retrieved " + instructors.size() + " instructors");
            return instructors;
        } catch (Exception e) {
            System.err.println("❌ Error retrieving instructors: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }

    // Get active instructors only
    public List<Instructor> getActiveInstructors() {
        try {
            List<Instructor> instructors = instructorDAO.findByStatus("Active");
            System.out.println("✅ Retrieved " + instructors.size() + " active instructors");
            return instructors;
        } catch (Exception e) {
            System.err.println("❌ Error retrieving active instructors: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }

    // Update instructor
    public boolean updateInstructor(Instructor instructor) {
        try {
            // Validate instructor exists
            Instructor existingInstructor = instructorDAO.findById(instructor.getInstructorId());
            if (existingInstructor == null) {
                System.out.println("❌ Instructor not found for update: ID " + instructor.getInstructorId());
                return false;
            }

            // Update the instructor
            boolean success = instructorDAO.update(instructor);
            if (success) {
                System.out.println("✅ Instructor updated successfully: " + instructor.getName() + " (ID: " + instructor.getInstructorId() + ")");
            } else {
                System.out.println("❌ Failed to update instructor: " + instructor.getName());
            }
            return success;
        } catch (Exception e) {
            System.err.println("❌ Error updating instructor: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    // Delete instructor (soft delete by setting status to inactive)
    public boolean deleteInstructor(int instructorId) {
        try {
            // Validate instructor exists
            Instructor existingInstructor = instructorDAO.findById(instructorId);
            if (existingInstructor == null) {
                System.out.println("❌ Instructor not found for deletion: ID " + instructorId);
                return false;
            }

            // Soft delete by setting status to inactive
            existingInstructor.setStatus("Inactive");
            boolean success = instructorDAO.update(existingInstructor);
            
            if (success) {
                System.out.println("✅ Instructor deactivated successfully: ID " + instructorId);
            } else {
                System.out.println("❌ Failed to deactivate instructor: ID " + instructorId);
            }
            return success;
        } catch (Exception e) {
            System.err.println("❌ Error deleting instructor: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    // Hard delete instructor (permanent removal)
    public boolean hardDeleteInstructor(int instructorId) {
        try {
            boolean success = instructorDAO.delete(instructorId);
            if (success) {
                System.out.println("✅ Instructor permanently deleted: ID " + instructorId);
            } else {
                System.out.println("❌ Failed to permanently delete instructor: ID " + instructorId);
            }
            return success;
        } catch (Exception e) {
            System.err.println("❌ Error hard deleting instructor: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    // Search instructors by name or identifier
    public List<Instructor> searchInstructors(String keyword) {
        try {
            List<Instructor> instructors = instructorDAO.searchByKeyword(keyword);
            System.out.println("✅ Found " + instructors.size() + " instructors matching: " + keyword);
            return instructors;
        } catch (Exception e) {
            System.err.println("❌ Error searching instructors: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }

    // Get instructors by status
    public List<Instructor> getInstructorsByStatus(String status) {
        try {
            List<Instructor> instructors = instructorDAO.findByStatus(status);
            System.out.println("✅ Retrieved " + instructors.size() + " instructors with status: " + status);
            return instructors;
        } catch (Exception e) {
            System.err.println("❌ Error retrieving instructors by status: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }

    // Get instructors by location
    public List<Instructor> getInstructorsByLocation(String location) {
        try {
            List<Instructor> instructors = instructorDAO.findByLocation(location);
            System.out.println("✅ Retrieved " + instructors.size() + " instructors from location: " + location);
            return instructors;
        } catch (Exception e) {
            System.err.println("❌ Error retrieving instructors by location: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }

    // Check if instructor identifier already exists
    public boolean instructorIdentifierExists(String identifier) {
        try {
            boolean exists = instructorDAO.identifierExists(identifier);
            if (exists) {
                System.out.println("ℹ️ Instructor identifier already exists: " + identifier);
            }
            return exists;
        } catch (Exception e) {
            System.err.println("❌ Error checking instructor identifier: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    // Get instructor count
    public int getInstructorCount() {
        try {
            int count = instructorDAO.getTotalCount();
            System.out.println("ℹ️ Total instructors in system: " + count);
            return count;
        } catch (Exception e) {
            System.err.println("❌ Error getting instructor count: " + e.getMessage());
            e.printStackTrace();
            return 0;
        }
    }

    // Get active instructor count
    public int getActiveInstructorCount() {
        try {
            int count = instructorDAO.getCountByStatus("Active");
            System.out.println("ℹ️ Active instructors in system: " + count);
            return count;
        } catch (Exception e) {
            System.err.println("❌ Error getting active instructor count: " + e.getMessage());
            e.printStackTrace();
            return 0;
        }
    }

    // Update instructor status
    public boolean updateInstructorStatus(int instructorId, String status) {
        try {
            Instructor instructor = instructorDAO.findById(instructorId);
            if (instructor == null) {
                System.out.println("❌ Instructor not found: ID " + instructorId);
                return false;
            }

            instructor.setStatus(status);
            boolean success = instructorDAO.update(instructor);
            if (success) {
                System.out.println("✅ Instructor status updated to '" + status + "' for ID: " + instructorId);
            } else {
                System.out.println("❌ Failed to update instructor status for ID: " + instructorId);
            }
            return success;
        } catch (Exception e) {
            System.err.println("❌ Error updating instructor status: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    // Validate instructor data
    public boolean validateInstructorData(String name, String identifier) {
        if (name == null || name.trim().isEmpty()) {
            System.out.println("❌ Instructor name cannot be empty");
            return false;
        }
        if (identifier == null || identifier.trim().isEmpty()) {
            System.out.println("❌ Instructor identifier cannot be empty");
            return false;
        }
        if (name.length() > 100) {
            System.out.println("❌ Instructor name too long (max 100 characters)");
            return false;
        }
        if (identifier.length() > 50) {
            System.out.println("❌ Instructor identifier too long (max 50 characters)");
            return false;
        }
        return true;
    }

    // Get available instructor statuses
    public List<String> getAvailableStatuses() {
        try {
            List<String> statuses = instructorDAO.getDistinctStatuses();
            System.out.println("✅ Retrieved " + statuses.size() + " available instructor statuses");
            return statuses;
        } catch (Exception e) {
            System.err.println("❌ Error retrieving instructor statuses: " + e.getMessage());
            e.printStackTrace();
            // Fallback - Java 8 compatible
            List<String> fallbackStatuses = new ArrayList<>();
            fallbackStatuses.add("Active");
            fallbackStatuses.add("Inactive");
            fallbackStatuses.add("On Leave");
            fallbackStatuses.add("Terminated");
            return fallbackStatuses;
        }
    }

    // Get instructor statistics
    public void printInstructorStatistics() {
        try {
            int totalInstructors = instructorDAO.getTotalCount();
            int activeInstructors = instructorDAO.getCountByStatus("Active");
            List<String> statuses = instructorDAO.getDistinctStatuses();
            List<String> locations = instructorDAO.getDistinctLocations();
            
            System.out.println("\n📊 INSTRUCTOR STATISTICS");
            System.out.println("========================");
            System.out.println("Total Instructors: " + totalInstructors);
            System.out.println("Active Instructors: " + activeInstructors);
            System.out.println("Inactive Instructors: " + (totalInstructors - activeInstructors));
            
            System.out.println("\nStatus Distribution:");
            for (String status : statuses) {
                int count = instructorDAO.getCountByStatus(status);
                System.out.println(" - " + status + ": " + count + " instructors");
            }
            
            System.out.println("\nLocations:");
            for (String location : locations) {
                int count = instructorDAO.getCountByLocation(location);
                System.out.println(" - " + location + ": " + count + " instructors");
            }
        } catch (Exception e) {
            System.err.println("❌ Error generating instructor statistics: " + e.getMessage());
        }
    }
}